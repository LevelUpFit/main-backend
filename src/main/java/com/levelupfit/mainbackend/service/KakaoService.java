package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.enums.ProviderTypeEnum;
import com.levelupfit.mainbackend.domain.user.SocialUser;
import com.levelupfit.mainbackend.domain.user.User;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.user.response.LoginResponse;
import com.levelupfit.mainbackend.repository.SocialUserRepository;
import com.levelupfit.mainbackend.repository.UserRepository;
import com.levelupfit.mainbackend.util.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserService userService;
    private final SocialUserRepository socialUserRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    @Value("${KAKAO_RESTAPI_KEY}")
    private String client_id;

    @Value("${KAKAO_REDIRECT_URI}") //반환 URI
    private String redirect_uri;

    //로그인 페이지 요청
    public String loginPage() {
        return  "https://kauth.kakao.com/oauth/authorize?response_type=code&prompt=login"
                + "&client_id=" + client_id
                + "&redirect_uri=" + redirect_uri;
    }

    //카카오 access토큰 가져오는 메서드
    public String getAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        try {
            // RestTemplate 인스턴스 생성
            RestTemplate restTemplate = new RestTemplate();

            // HTTP 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // 요청 파라미터 설정
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", client_id);
            params.add("redirect_uri", redirect_uri);
            params.add("code", code);

            // 요청 엔티티 생성
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

            // POST 요청 실행 및 응답 받기
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, requestEntity, String.class);

            // 응답 JSON 파싱
            JSONObject json = new JSONObject(response.getBody());
            return json.getString("access_token");

        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve access token from Kakao.", e);
        }
    }

    //유저 정보 가져오는 메서드
    public JSONObject getUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        try{
            URL url = new URL(userInfoUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = con.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Failed to retrieve user info. Response Code: " + responseCode);
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while( (inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new JSONObject(response.toString());
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    //카카오 로그인 핸들링 accessToken -> userinfo -> email -> 신규/기존 회원 판별
    @Transactional
    public ApiResponse<LoginResponse> handleKakaoLogin(String code) {
        System.out.println("Authorization code: " + code);
        HashMap<String, String> result =   new HashMap<>();
        // Access Token 요청
        String accessToken = getAccessToken(code);

        if (accessToken == null) {
            throw new RuntimeException("Failed to retrieve access token.");
        }

        // 사용자 정보 요청
        JSONObject userInfo = getUserInfo(accessToken);
        if (userInfo == null) {
            throw new RuntimeException("Failed to retrieve user info.");
        }

        // 이메일 추출
        JSONObject email = userInfo.getJSONObject("kakao_account");
        if (!email.has("email")) { //email 정보가 있는지 없는지 확인
            throw new RuntimeException("Email not provided by Kakao.");
        }

        String emailstr = email.getString("email");

        //로직 구현 필요 기존 회원이 있으면 연동 없으면 생성
        // 신규/기존 회원 확인
        try{
            if(!socialUserRepository.existsByEmail(emailstr)) {
                String userAccessToken = jwtUtils.createAccessToken(emailstr);
                String refreshToken = jwtUtils.createRefreshToken(emailstr);

                if(!userRepository.existsByEmail(emailstr)){
                    User user = User.builder()
                            .email(emailstr)
                            .nickname("헬린이1")
                            .dob(LocalDate.parse("9000-12-31"))
                            .level(1)
                            .gender("male")
                            .profile("test")
                            .access_token(userAccessToken)
                            .refresh_token(refreshToken)
                            .build();

                    User saveduser = userRepository.save(user);

                    SocialUser socialUser = SocialUser.builder()
                            .user(saveduser) //여기 로직 손봐야함
                            .providerType(ProviderTypeEnum.KAKAO)
                            .email(emailstr)
                            .build();
                    socialUserRepository.save(socialUser);

                    LoginResponse response = new LoginResponse();
                    response.setUserId(saveduser.getUserid());
                    response.setNickname(saveduser.getNickname());
                    response.setProfile(saveduser.getProfile());
                    response.setLevel(saveduser.getLevel());
                    response.setAccessToken(accessToken);
                    response.setRefreshToken(refreshToken);

                    return ApiResponse.ok(response,201);
                }
            } else {

                User user = userRepository.findByEmail(emailstr);
                LoginResponse response = new LoginResponse();
                response.setUserId(user.getUserid());
                response.setNickname(user.getNickname());
                response.setProfile(user.getProfile());
                response.setLevel(user.getLevel());
                response.setAccessToken(user.getAccess_token());
                response.setRefreshToken(user.getRefresh_token());
                return ApiResponse.ok(response,200);
            }

        } catch (Exception e) {
            return ApiResponse.fail("소셜 로그인 오류",500);
        }
        return ApiResponse.fail("알 수 없는 오류",500);
    }

    //많이 생각해봐야할듯 ㅇㅇ
    //같은 email의 form회원으로 로그인을 했는지
//    public boolean checkLinkSocial(String email){
//        return formUserRepository.existsByEmail(email);
//    }
}
