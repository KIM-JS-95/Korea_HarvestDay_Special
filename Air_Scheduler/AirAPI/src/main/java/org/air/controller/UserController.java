package org.air.controller;

import lombok.extern.slf4j.Slf4j;
import org.air.config.HeaderSetter;
import org.air.entity.*;
import org.air.jwt.JwtTokenProvider;
import org.air.service.CustomUserDetailService;
import org.air.service.EmailService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author : KIM JAE SEONG <br>
 * Content: 사용자 관리 기능 모음집 <br>
 * Function <br>
 * join: 회원가입 <br>
 * login: 유저 로그인 <br>
 * log_out: 유저 로그 아웃 <br>
 */
@Slf4j
@RestController
public class UserController {
    @Autowired
    private HeaderSetter headerSetter;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;


    @GetMapping("/servertest")
    public ResponseEntity server_test(){

        String notice ="소프트파워 개인정보 처리방침\n" +
                "여러분의 개인정보의 안전한 처리는 소프트파워 에게 있어 가장 중요한 일 중 하나입니다. \n" +
                "여러분의 개인정보는 서비스의 원활한 제공을 위하여 수집됩니다. \n" +
                "법령에 의하거나 여러분이 별도로 동의하지 아니하는 한 소프트파워가 여러분의 개인정보를 \n" +
                "제3자에게 제공하는 일은 결코 없으므로, 안심하셔도 좋습니다. 개인정보 처리방침이 바뀐 때에는 \n" +
                "여러분이 언제든지 그 내용과 이유를 쉽게 알 수 있도록 공지사항을 통하여 알려 드리겠습니다.\n" +
                "\n" +
                "여러분의 소중한 개인정보는 소프트파워 가 여러분에게 더 나은 서비스를 제공하기 위해 활용됩니다.\n" +
                "\n" +
                "1. 개인정보의 처리 목적\n" +
                "\n" +
                "소프트파워 는 서비스를 원활하게 제공하고 더욱 향상된 이용자 경험을 드리기 위해 필요한 여러분의 \n" +
                "개인정보를 수집합니다. 즉, 여러분이 설치 시 동의한 권한에 따라 서비스의 기본 기능이나 여러 특화된\n" +
                "기능을 제공하기 위해서 연락처 등의 기능은 새로운 서비스 개발 및 특화, 맞춤형 서비스 제공, 기능개\n" +
                "선, 법령 등에 규정된 의무의 이행, 법령이나 이용약관에 반하여 여러분에게 피해를 줄 수 있는 잘못된\n" +
                "이용행위의 방지를 위해서도 여러분의 개인정보가 활용됩니다.\n" +
                "\n" +
                "2. 처리하는 개인정보 항목\n" +
                "\n" +
                "소프트파워가 더 나은 서비스를 제공해 드리기 위해 수집하는 여러분의 개인정보는 아래와 같습니다.\n" +
                "소프트파워는 여러분이 서비스에 처음 가입할 때 또는 서비스를 이용하는 과정에서 홈페이지 또는 \n" +
                "서비스 내 개별 어플리케이션이나 프로그램 등을 통하여 여러분의 전화번호, 단말기 기타기기정보 (개별 기기, 브라우저 또는 앱과 관련된 식별자입니다. IMEI 번호, MAC 주소), 스마트폰 등 단말기 연락\n" +
                "처, 사진, 카메라, 오디오, GPS, 주소록읽기 등이 선택적으로 수집됩니다.\n" +
                "\n" +
                "소프트파워는 여러분의 별도 동의가 있는 경우나 법령에 규정된 경우를 제외하고는 여러분의 개인정보\n" +
                "를 절대 제3자에게 제공하지 않습니다.\n" +
                "개인정보는 부정한 이용으로 인한 분쟁을 방지하기 위한 내부방침에 따라 보존한 다음 파기를 요청한 \n" +
                "사용자에 한해 파기됩니다.\n" +
                "\n" +
                "3. 개인정보 수집 방법\n" +
                "\n" +
                "앱 등의 프로그램을 사용하기 위해 수신 동의를 하는 사용자에 한해 해당 개인정보를 수집합니다.\n" +
                "\n" +
                "4. 자동수집 장치의 운용 및 거부\n" +
                "회사는 이용자 개개인에게 개인화되고 맞춤화된 서비스를 제공하기 위해 이용자의\n" +
                "정보를 저장하고 수시로 불러오는 '쿠키(cookie)'를 사용합니다.\n" +
                "① 쿠키의 사용 목적\n" +
                "회원과 비회원의 접속 빈도나 방문 시간 등의 분석, 이용자의 취향과 관심분야의\n" +
                "파악 및 자취 추적, 각종 이벤트 참여 정도 및 방문 회수 파악\n" +
                "\n" +
                "5. 이용자 및 법정대리인의 권리와 그 행사방법\n" +
                "① 정보주체는 회사에 대해 언제든지 다음 각 호의 개인정보 보호 관련 권리를 행사할\n" +
                "수 있습니다.\n" +
                "1) 개인정보 열람 요구\n" +
                "2) 오류 등이 있을 경우 정정 요구\n" +
                "3) 삭제 요구\n" +
                "\n" +
                "6. 개인정보의 처리 및 보유기간\n" +
                "① 회사는 법령에 따른 개인정보 보유·이용기간 또는 이용자로 부터 개인정보를\n" +
                "수집 시에 동의 받은 개인정보 보유·이용기간 내에서 개인정보를처리·보유합니다.\n" +
                "② 각각의 개인정보 처리 및 보유 기간은 다음과 같습니다.\n" +
                "1) 스마트폰 앱 회원 가입 및 관리 : 탈퇴 요청시 까지\n" +
                "가. 관계 법령 위반에 따른 수사․조사 등이 진행 중인 경우에는 해당 수사․조사\n" +
                "종료 시 까지\n" +
                "나. 스마트폰 앱 이용에 따른 채권․채무관계 잔존 시에는 해당 채권·채무관계\n" +
                "정산 시 까지\n" +
                "2) 재화 또는 서비스 제공 : 재화·서비스 공급완료 및 요금결제․정산 완료시까지\n" +
                "다만, 다음의 사유에 해당하는 경우에는 해당 기간 종료 시 까지\n" +
                "가.「전자상거래 등에서의 소비자 보호에 관한 법률」에 따른 표시·광고,\n" +
                "계약내용 및 이행 등 거래에 관한 기록\n" +
                "나.「통신비밀보호법」제41조에 따른 통신사실 확인자료 보관\n" +
                "\n" +
                "7. 개인정보의 파기\n" +
                "① 회사는 개인정보를 파기 요청시 바로 파기합니다.\n" +
                "② 이용자로부터 동의 받은 개인정보 보유기간이 경과하거나 처리목적이 달성\n" +
                "되었음에도 불구하고 다른 법령에 따라 개인정보를 계속 보존하여야 하는 경우에는,\n" +
                "해당 개인정보를 별도의 데이터베이스(DB)로 옮기거나 보관 장소를 달리하여\n" +
                "보존합니다.\n" +
                "③ 개인정보 파기의 절차 및 방법은 다음과 같습니다.\n" +
                "1) 파기절차\n" +
                "회사는 파기 사유가 발생한 개인정보를 선정하고, 회사의 개인정보 보호책임자의\n" +
                "승인을 받아 개인정보를 파기합니다.\n" +
                "2) 파기방법\n" +
                "회사는 전자적 파일 형태로 기록 저장된 개인정보는 기록을 재생할 수 없도록 파기합니다.\n" +
                "\n" +
                "[개인정보보호 책임자]\n" +
                "이름: 김건준 연락처: 고객센터, [02-557-7800, gjkim@smartmaker.com]\n" +
                " \n" +
                "소프트파워는 법률이나 서비스의 변경사항을 반영하기 위한 목적 등으로 개인정보 처리방침을 수정할\n" +
                "수 있습니다. 개인정보 처리방침이 변경되는 경우 소프트파워는 변경 사항을 게시하며, 변경된 개인정보\n" +
                "처리방침은 게시한 날로부터 7일 후부터 효력이 발생합니다. 하지만, 피치 못하게 여러분의 권리에 \n" +
                "중요한 변경이 있을 경우 변경될 내용을 30일 전에 미리 알려드리겠습니다.\n" +
                "\n" +
                "일자: 2019년 10월 10일\n" +
                "(주) 소프트파워에서 제공하는 서비스에 대한 개인정보 처리방침 입니다.";
        return ResponseEntity.ok()
                .headers(headerSetter.haederSet("", "login Success"))
                .body(notice);
    }

    // 로그인 이메일 주소 빼고 jejuair.nat으로 만
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserDTO user) throws MessagingException, IOException {
        User member = customUserDetailService.loadUserByUser(user);

        if (member == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("");
        } else { // 안드로이드 ID값이 다르면? -> 임시 계정 테이블에 저장 -> 이메일로 전달
            if (!user.getAndroidid().equals(member.getAndroidid())) {
                TemppilotcodeDAO temppilotcodeDAO = TemppilotcodeDAO.builder()
                        .email(member.getEmail())
                        .username(member.getName())
                        .userid(member.getUserid())
                        .androidid(user.getAndroidid())
                        .build();
                System.out.println(user.getAndroidid());
                customUserDetailService.login_check(temppilotcodeDAO); // 임시저장
                emailService.sendLoginCautionMail(member.getName(), member.getEmail(), member.getUserid(), user.getAndroidid());
                return ResponseEntity.status(Integer.parseInt(StatusEnum.DEVICE_NOT_MATCH.getStatusCode()))
                        .headers(headerSetter.haederSet("", StatusEnum.DEVICE_NOT_MATCH.getMessage()))
                        .body("");
            }
            Date date = new Date();
            SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
            String token = jwtTokenProvider.createToken(member.getUserid(), access_time.format(date));
            customUserDetailService.token_save(member, token);
            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(token, "login Success"))
                    .body("Login Success");
        }
    }

    @PostMapping("/user_modify")
    public ResponseEntity modify(@RequestHeader("Authorization") String token, @RequestBody User user) {

        String user_string = jwtTokenProvider.getUserPk(token); // body: userid
        boolean flag = customUserDetailService.modify(user, user_string);
        if (flag) {
            Date date = new Date();
            SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
            String new_token = jwtTokenProvider.createToken(user.getUserid(), access_time.format(date));

            User member = customUserDetailService.loadUserByToken(user.getUserid());
            customUserDetailService.token_save(member, token);

            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(new_token, "login Success"))
                    .body("Login Success");
        } else {
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()))
                    .headers(headerSetter.haederSet(token, "SAVE ERROR"))
                    .body("");
        }
    }

    @PostMapping("/getuserinfobyToken")
    public ResponseEntity getUserinfo(@RequestHeader("Authorization") String token) {
        String user_token = jwtTokenProvider.getUserPk(token); // body: userid
        User user = customUserDetailService.loadUserByToken(user_token);

        // Create a JSON object with required fields
        JSONObject member = new JSONObject();
        member.put("userid", user.getUserid());
        member.put("email", user.getEmail());
        member.put("password", user.getPassword());

        return ResponseEntity.ok()
                .headers(headerSetter.haederSet(token, "login Success"))
                .body(member.toString());
    }

    // 로그아웃 (서블렛 토큰 제거)
    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String token) {
        String user_string = jwtTokenProvider.getUserPk(token);

        if (customUserDetailService.logout(user_string)) {
            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(token, "logout fail"))
                    .body("");
        } else {
            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(null, "logout Success"))
                    .body("");
        }
    }

    @PostMapping("/user/delete")
    public ResponseEntity remove_user(@RequestHeader("Authorization") String token) {
        String user_string = jwtTokenProvider.getUserPk(token);

        if (customUserDetailService.delete_user(user_string)) {
            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(token, "delete fail"))
                    .body("");
        } else {
            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(null, "delete Success"))
                    .body("");
        }
    }


    // ----------------------------------- 회원가입 권한 -----------------------------------

    // 회원가입 이전 임시 저장 -> 해당 유저에게 이메일을 전달후(랜덤키 전달) 회원가입 처리 : <userid>@jejuair.net
    @PostMapping("/join/save/pilotcode")
    public ResponseEntity join(@RequestBody TemppilotcodeDAO temppilotcode) {
        Temppilotcode result = customUserDetailService.save_pilotcode(temppilotcode);
        if (result != null) {
            boolean emailSent = emailService.sendTokenMail(result.getUsername(), result.getEmail(), result.getRandomkey());
            if (emailSent) { // 메일 & 임시 회원 성공
                return ResponseEntity.status(HttpStatus.CREATED).body("");
            } else { // 메일 전송에 실패
                customUserDetailService.delete_temp_pilotcode(temppilotcode);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    // 최종 회원가입 (디바이스 토큰도 같이 획득)
    @PostMapping("/join/save/user/fin")
    public ResponseEntity join_family_fin(@RequestBody UserDTO user) { // password, randomkey, androidid
        HttpStatus status = HttpStatus.CREATED;

        int result = customUserDetailService.savePilot(user);
        if (result == 0) { // 존재하지 않는
            status = HttpStatus.valueOf(Integer.parseInt(StatusEnum.NOT_FOUND.getStatusCode()));
        }

        return ResponseEntity.status(status).body("");
    }

    @PostMapping("/join/save/family")
    public ResponseEntity join_family(@RequestBody UserDTO user) throws MessagingException { // family_id ,userid, password, androidid
        User user1 = customUserDetailService.getUser(user.getFamily_id());
        TemppilotcodeDAO temppilotcodeDAO = TemppilotcodeDAO.builder()
                .userid(user.getUserid()) // 가족 아이디
                .password(user.getPassword()) // 가족 비번
                .username(user.getName()) // 가족 이름
                .pilotid(user1.getUserid())
                .build();
        customUserDetailService.processPilotcode(temppilotcodeDAO); // 임시저장

        String mgs = user.getName()+"님과 일정을 공유합니다.";
        emailService.sendMail(user.getUserid(), user1.getName(), user.getName(), user1.getEmail(), mgs, user.getAndroidid());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("");
    }


    @GetMapping("/join/save/family/fin")
    public ResponseEntity join_family_fin(@RequestParam("userid") String userid, @RequestParam("androidid") String androidid){
        User family = customUserDetailService.saveFamily(userid, androidid);

        HttpStatus status = HttpStatus.CREATED;
        if (family==null) { // 저장 실패
            status = HttpStatus.valueOf(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()));
        }

        return ResponseEntity.status(status).body("");
    }


    // 유저 아이디 체크
    @GetMapping("/join/check/user") // https://~~?userid=123
    public ResponseEntity check_user(@RequestParam("userid") String userid){

        HeaderSetter headers = new HeaderSetter();
        if(customUserDetailService.exist_userid(userid)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers.haederSet("", StatusEnum.NOT_FOUND.getMessage()))
                    .body("");
        }else{
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.NOT_FOUND.getStatusCode()))
                    .headers(headers.haederSet("", StatusEnum.NOT_FOUND.getMessage()))
                    .body("");
        }
    }

}