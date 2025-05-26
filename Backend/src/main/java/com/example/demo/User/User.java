package com.example.demo.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="users")
@SequenceGenerator(initialValue=1,allocationSize=1,name="seq_users",sequenceName="seq_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User{

    @Id
    @GeneratedValue(
        generator="seq_users",
        strategy=GenerationType.SEQUENCE
    )
    private Long id;/* DB에 저장할 기본키 값 */

    private String username;/* 유저의 ID */
    private String password;/* 유저의 비밀번호 */
    
    private String name;/* 유저의 이름 */
    private String phone;/* 유저의 전화번호 ( 예 : "01055661234" ) */   
    private String email;/* 유저의 이메일 ( 예 : "abc1234@naver.com" ) */
    private String birthdate;/* 유저의 생년월일 ( 예 : "2001-03-06" ) */
    private String gender;/* 유저의 성별 ( 예 : "남성" ) */
    private String zipcode;/* 유저의 우편번호 ( 예 : "33556" ) */
    private String address;/* 유저의 기본주소 ( 예 : "경기도 파주시" ) */
    private String address_detail;/* 유저의 상세주소 ( 예 : "충현로 38번길 25" ) */
    private String auth;/* 유저의 권한 ( 예 : "USER" ) */
    private String platform;/* OAuth2 유저의 경우, 로그인한 플랫폼 */
    private String createdate;/* 계정 생성 날짜 */

}