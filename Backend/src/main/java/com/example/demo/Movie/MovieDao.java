package com.example.demo.Movie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

@Component
public class MovieDao {
    @Autowired
    private MovieRepository repo;

    public List<Movie> findAll() {
        return repo.findAll();
    }

    public Movie findById(Long id) {
        return repo.findFirstById(id);
    }

    public void save(Movie movie) {
        repo.save(movie);
    }


    public List<Movie> findForMoviePage() {
        return repo.findIdAndRankAndDescriptionAndScoreAndTitleAndRateAndReleaseDateAndLikeNumberAndPosterAndLabelAndReserveRateByOrderByIdAsc();
    }



    // 초기 데이터 입력
    @PostConstruct
    @Transactional
    public void insertTestMovies() {
        if(repo.count() > 0) return;
        save(new Movie(null, 
            "필즈 오브 데스티니", "Fields of Destiny", 
            "12", "2023.05.16", 
            "씨앗은 땅에만 뿌려지는 것이 아니다. 희망과 용기, 사랑도 이 황무지 위에서 자란다. 대공황의 그늘 아래, 미국 중부의 한 농장을 배경으로 펼쳐지는 한 가족의 세대 간 갈등과 화해, 그리고 삶의 터전을 지켜내려는 치열한 이야기. 하늘을 나는 꿈을 품은 딸, 땅을 지키려는 아버지, 이상과 현실 사이에서 갈등하는 오빠. 그들은 각자의 방식으로 ‘운명의 들판’을 가꾸며 자신만의 길을 찾아 나선다.", 
            90, "드라마, 시대극, 가족", 
            "엘리자 레이먼드", "클레어 하딩턴,루카스 멜든,윌터 그레인", 
            9.8, 5300L, 
            "https://live.staticflickr.com/65535/52900840054_14af3e493c_z.jpg", 
            "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700795880/catalog/1600659718750367744/xiry6ufbjttckqxpfzrw.jpg", 
            // new String[] {}, 
            "", 20.1, 2000L, 1
        ));
        save(new Movie(null, 
            "킬러 어드바이스", "Killer Advice", 
            "19", "2021.02.05", 
            "When Beth (Kate Watson) suffers a traumatic attack, her family and friends suggest she see a therapist to help her cope. However, her new therapist gives her more than she bargained for.", 
            89, "서스팬스, 스릴러, 범죄물",
            "자레드 콘", "케이트 왓슨, 지지 거스틴,메러디스 토마스,에릭 로버츠",
            9.8, 2100L, 
            "https://m.media-amazon.com/images/M/MV5BYzgxZjY5MGQtODRhZi00ZDQyLWE5MDQtYTk3NGJkN2I5M2M1XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
            "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700796426/catalog/1600659718750367744/iqmiudmmo6s7zcofwmpf.jpg",
            // new String[] {},
            "4DX", 19.8, 1900L, 2
        ));
        save(new Movie(null, 
            "인터스텔라", "InterStella", 
            "12", "2014.11.06", 
            "세계 각국의 정부와 경제가 완전히 붕괴된 미래가 다가온다. 지난 20세기에 범한 잘못이 전 세계적인 식량 부족을 불러왔고, NASA도 해체되었다. 이때 시공간에 불가사의한 틈이 열리고, 남은 자들에게는 이 곳을 탐험해 인류를 구해야 하는 임무가 주어진다. 사랑하는 가족들을 뒤로 한 채 인류라는 더 큰 가족을 위해, 그들은 이제 희망을 찾아 우주로 간다. 그리고 우린 답을 찾을 것이다. 늘 그랬듯이...",
            169, "SF, 모험, 재난, 가족", 
            "크리스토퍼 놀란", "매튜 매커너히,앤 해서웨이,제시카 차스테인,마이클 케인",
            9.8, 1500L, 
            "https://rukminim2.flixcart.com/image/850/1000/l2dmky80/poster/y/f/b/small-poster-interstellar-sl407-wall-poster-13x19-inches-matte-original-imagdqezkfchjkhz.jpeg?q=20&crop=false", 
            "https://i.ytimg.com/vi/YF1eYbfbH5k/maxresdefault.jpg?sqp=-oaymwEmCIAKENAF8quKqQMa8AEB-AH-CYAC0AWKAgwIABABGE4gYShlMA8=&rs=AOn4CLDt6JPoesvmQnP8qf-00JpeDZUfyA",
            // new String[] {}, 
            "IMAX", 18.7, 1800L, 3
        ));
        save(new Movie(null, 
            "내 이름은 알프레드 히치콕", "My Name is Alfred Hitchcock", 
            "ALL", "2022.09.05", 
            "내 이름은 알프레드 히치콕(My Name Is Alfred Hitchcock)은 마크 커즌스(Mark Cousins)가 각본과 감독을 맡은 2022년 영국 다큐멘터리 영화입니다. 영국의 영화감독 알프레드 히치콕에 관한 이야기이다", 
            120, "다큐멘터리", 
            "마크 코신스", "알리스테어 맥고완", 
            9.8, 1300L, 
            "https://www.mvtimes.com/mvt/uploads/2024/11/film-my-name-alfred-hitchcock-2.jpg", 
            "https://theknockturnal.com/wp-content/uploads/2024/11/maxresdefault-3.jpg", 
            // new String[] {}, 
            "", 17.6, 1700L, 4
        ));
        save(
            new Movie(null, 
            "어벤져스: 엔드게임", "Avengers: Endgame", 
            "15", "2019.04.24", 
            "인피니티 워 이후 많은 사람이 죽고 또 많은 것을 잃게 된 지구는 더 이상 희망이 남지 않아 절망 속에 살아간다. 전쟁 후 남아 있던 어벤저스는 그런 그들의 모습을 보게 된다. 마지막으로 지구를 살리려 모든 것을 건 타노스와 최후의 전쟁을 치른다.", 
            181, "슈퍼히어로, SF, 액션, 판타지", 
            "앤서니 루소, 조 루소", "로버트 다우니 주니어,크리스 에반스,마크 러팔로,크리스 헴스워스,스칼렛 요한슨,제레미 레너", 
            9.8, 986L, 
            "https://upload.wikimedia.org/wikipedia/ko/thumb/f/f2/%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4-_%EC%97%94%EB%93%9C%EA%B2%8C%EC%9E%84_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg/1200px-%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4-_%EC%97%94%EB%93%9C%EA%B2%8C%EC%9E%84_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg", 
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjvX4mwUQtb-pTitJuZAbutp6dgMG9YXojLQ&s", 
            // new String[] {"https://i.namu.wiki/i/vFPYI_yGi_9pqUocLkpKYslKfBawVtk1IUdKA069QOcmZSHsTyVuU5P4CQ6CYGXDpDGZ0jTIDZr-ZnVIEU5Z3w.webp","https://i.ytimg.com/vi/ijUsSpRVhBU/maxresdefault.jpg"}, 
            "IMAX, 4DX", 16.5, 1600L, 5
        ));
        save(new Movie(null, 
            "범죄도시4", "THE ROUNDUP : PUNISHMENT", 
            "15", "2024.04.24", 
            "신종 마약 사건 3년 뒤, 괴물형사 ‘마석도’(마동석)와 서울 광수대는 배달앱을 이용한 마약 판매 사건을 수사하던 중 수배 중인 앱 개발자가 필리핀에서 사망한 사건이 대규모 온라인 불법 도박 조직과 연관되어 있음을 알아낸다. 필리핀에 거점을 두고 납치, 감금, 폭행, 살인 등으로 대한민국 온라인 불법 도박 시장을 장악한 특수부대 용병 출신의 빌런 ‘백창기’(김무열)와 한국에서 더 큰 판을 짜고 있는 IT업계 천재 CEO ‘장동철’(이동휘). ‘마석도’는 더 커진 판을 잡기 위해 ‘장이수’(박지환)에게 뜻밖의 협력을 제안하고 광역수사대는 물론, 사이버수사대까지 합류해 범죄를 소탕하기 시작하는데… 나쁜 놈 잡는데 국경도 영역도 제한 없다! 업그레이드 소탕 작전! 거침없이 싹 쓸어버린다!", 
            109, "액션, 범죄, 스릴러, 형사", 
            "허명행", "마동석,김무열,박지환, 이동휘", 
            9.8, 734L, 
            "https://i.namu.wiki/i/KwJ2dfIySu2k8JWlK3nD-gS7A9G-2I2EWKkNjoVRqaHabjK88STUo8FXi545XV6Pe8ERSX5DjF4e5k0IkOvznQ.webp",
            "https://biz.chosun.com/resizer/v2/XIXAJIUJ6YFR5YLGQDRVYHLJ54.jpg?auth=ca0a12238befb32839c719e6beedf5e71aa7684834c2bf17e3993c4530d0f706",
            // new String[] {}, 
            "IMAX", 15.4, 1500L, 6
        ));
        save(new Movie(null, 
            "귀멸의칼날 무한성편", 
            "Demon Slayer: Kimetsu No Yaiba The Movie: Infinity Castle", 
            "19", "2025.08.22", 
            "혈귀로 변한 여동생 ‘네즈코’를 되돌리기 위해 귀살대가 된 ‘탄지로’! 어둠 속을 달리는 무한열차에서 승객들이 흔적 없이 사라진다는 소식에 ‘젠이츠’, ‘이노스케’와 함께 임무 수행을 위해 무한열차에 탑승한다. 그리고 그 곳에서 만난 귀살대 최강 검사 염주 ‘렌고쿠’! 이들은 무한열차에 숨어 있는 혈귀의 존재를 직감하고 모두를 구하기 위해 목숨을 건 혈전을 시작하는데… 그 칼로 악몽을 끊어라!", 
            117, "시대극 판타지, 액션", 
            "소토자키 하루오", "하나에 나츠키,키토 아카리,시모노 히로,마츠오카 요시츠구", 
            9.8, 521L, 
            "https://i.namu.wiki/i/YvPBZ1kzk8Dku4HhOC2FGB7xKVXj5bpg8cSdRWsAZg-3Knqu5LcWJchrZDIVmz-08V3OV9uFLMfCRNCZRcnTxQ.webp", 
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYEAVqhsnZC3KnuJ_JMtuHSI7WEBwFc1as3w&s", 
            // new String[] {},
            "", 14.3, 1400L, 7
        ));
        save(new Movie(null, 
            "승부", "The Match", 
            "12", "2025.03.26", 
            "세계 최고 바둑 대회에서 국내 최초 우승자가 된 조훈현. 전 국민적 영웅으로 대접받던 그는 바둑 신동이라 불리는 이창호를 제자로 맞는다. “실전에선 기세가 8할이야” 제자와 한 지붕 아래에서 먹고 자며 가르친 지 수년. 모두가 스승의 뻔한 승리를 예상했던 첫 사제 대결에서 조훈현은 전 국민이 지켜보는 가운데, 기세를 탄 제자에게 충격적으로 패한다. 오랜만에 패배를 맛본 조훈현과 이제 승부의 맛을 알게 된 이창호 조훈현은 타고난 승부사적 기질을 되살리며 다시 한번 올라갈 결심을 하게 되는데…", 
            115, "드라마, 스포츠, 시대극", 
            "김형주", "이병헌,유아인", 
            9.8, 342L, 
            "https://img.megabox.co.kr/SharedImg/2025/03/27/O6RnDMOAnUw6geDdlaAXRlkqgy0mSSDb_420.jpg", 
            "https://i.namu.wiki/i/4sLX83VoaVVEDMA9Nmj9YCPnVnsnJSKIs7GEK4XSj89ofinn6FVUUjZKoIy2d5eYOgEL341pY-2Rq3PCk6c1MKAzfpVmYjfLfY0HbWjUfW1DhygfA8raXT49CaBTHWpz2gRQi49vsuXR4Y0CwInj_g.webp",
            // new String[] {}, 
            "IMAX", 13.2, 1300L, 8
        ));
        save(new Movie(null, 
            "필즈 오브 데스티니", "Fields of Destiny", 
            "12", "2023.05.16", 
            "씨앗은 땅에만 뿌려지는 것이 아니다. 희망과 용기, 사랑도 이 황무지 위에서 자란다. 대공황의 그늘 아래, 미국 중부의 한 농장을 배경으로 펼쳐지는 한 가족의 세대 간 갈등과 화해, 그리고 삶의 터전을 지켜내려는 치열한 이야기. 하늘을 나는 꿈을 품은 딸, 땅을 지키려는 아버지, 이상과 현실 사이에서 갈등하는 오빠. 그들은 각자의 방식으로 ‘운명의 들판’을 가꾸며 자신만의 길을 찾아 나선다.", 
            90, "드라마, 시대극, 가족", 
            "엘리자 레이먼드", "클레어 하딩턴,루카스 멜든,윌터 그레인", 
            9.8, 5300L, 
            "https://live.staticflickr.com/65535/52900840054_14af3e493c_z.jpg", 
            "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700795880/catalog/1600659718750367744/xiry6ufbjttckqxpfzrw.jpg", 
            // new String[] {}, 
            "", 12.1, 1200L, 9
        ));
        save(new Movie(null, 
            "킬러 어드바이스", "Killer Advice", 
            "19", "2021.02.05", 
            "When Beth (Kate Watson) suffers a traumatic attack, her family and friends suggest she see a therapist to help her cope. However, her new therapist gives her more than she bargained for.", 
            89, "서스팬스, 스릴러, 범죄물",
            "자레드 콘", "케이트 왓슨, 지지 거스틴,메러디스 토마스,에릭 로버츠",
            9.8, 2100L, 
            "https://m.media-amazon.com/images/M/MV5BYzgxZjY5MGQtODRhZi00ZDQyLWE5MDQtYTk3NGJkN2I5M2M1XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
            "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700796426/catalog/1600659718750367744/iqmiudmmo6s7zcofwmpf.jpg",
            // new String[] {},
            "4DX", 11.0, 1100L, 10
        ));
        save(new Movie(null, 
            "인터스텔라", "InterStella", 
            "12", "2014.11.06", 
            "세계 각국의 정부와 경제가 완전히 붕괴된 미래가 다가온다. 지난 20세기에 범한 잘못이 전 세계적인 식량 부족을 불러왔고, NASA도 해체되었다. 이때 시공간에 불가사의한 틈이 열리고, 남은 자들에게는 이 곳을 탐험해 인류를 구해야 하는 임무가 주어진다. 사랑하는 가족들을 뒤로 한 채 인류라는 더 큰 가족을 위해, 그들은 이제 희망을 찾아 우주로 간다. 그리고 우린 답을 찾을 것이다. 늘 그랬듯이...",
            169, "SF, 모험, 재난, 가족", 
            "크리스토퍼 놀란", "매튜 매커너히,앤 해서웨이,제시카 차스테인,마이클 케인",
            9.8, 1500L, 
            "https://rukminim2.flixcart.com/image/850/1000/l2dmky80/poster/y/f/b/small-poster-interstellar-sl407-wall-poster-13x19-inches-matte-original-imagdqezkfchjkhz.jpeg?q=20&crop=false", 
            "https://i.ytimg.com/vi/YF1eYbfbH5k/maxresdefault.jpg?sqp=-oaymwEmCIAKENAF8quKqQMa8AEB-AH-CYAC0AWKAgwIABABGE4gYShlMA8=&rs=AOn4CLDt6JPoesvmQnP8qf-00JpeDZUfyA",
            // new String[] {}, 
            "IMAX", 10.9, 1000L, 11
        ));
        save(new Movie(null, 
            "내 이름은 알프레드 히치콕", "My Name is Alfred Hitchcock", 
            "ALL", "2022.09.05", 
            "내 이름은 알프레드 히치콕(My Name Is Alfred Hitchcock)은 마크 커즌스(Mark Cousins)가 각본과 감독을 맡은 2022년 영국 다큐멘터리 영화입니다. 영국의 영화감독 알프레드 히치콕에 관한 이야기이다", 
            120, "다큐멘터리", 
            "마크 코신스", "알리스테어 맥고완", 
            9.8, 1300L, 
            "https://www.mvtimes.com/mvt/uploads/2024/11/film-my-name-alfred-hitchcock-2.jpg", 
            "https://theknockturnal.com/wp-content/uploads/2024/11/maxresdefault-3.jpg", 
            // new String[] {}, 
            "", 9.8, 900L, 12
        ));
        save(
            new Movie(null, 
            "어벤져스: 엔드게임", "Avengers: Endgame", 
            "15", "2019.04.24", 
            "인피니티 워 이후 많은 사람이 죽고 또 많은 것을 잃게 된 지구는 더 이상 희망이 남지 않아 절망 속에 살아간다. 전쟁 후 남아 있던 어벤저스는 그런 그들의 모습을 보게 된다. 마지막으로 지구를 살리려 모든 것을 건 타노스와 최후의 전쟁을 치른다.", 
            181, "슈퍼히어로, SF, 액션, 판타지", 
            "앤서니 루소, 조 루소", "로버트 다우니 주니어,크리스 에반스,마크 러팔로,크리스 헴스워스,스칼렛 요한슨,제레미 레너", 
            9.8, 986L, 
            "https://upload.wikimedia.org/wikipedia/ko/thumb/f/f2/%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4-_%EC%97%94%EB%93%9C%EA%B2%8C%EC%9E%84_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg/1200px-%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4-_%EC%97%94%EB%93%9C%EA%B2%8C%EC%9E%84_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg", 
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjvX4mwUQtb-pTitJuZAbutp6dgMG9YXojLQ&s", 
            // new String[] {"https://i.namu.wiki/i/vFPYI_yGi_9pqUocLkpKYslKfBawVtk1IUdKA069QOcmZSHsTyVuU5P4CQ6CYGXDpDGZ0jTIDZr-ZnVIEU5Z3w.webp","https://i.ytimg.com/vi/ijUsSpRVhBU/maxresdefault.jpg"}, 
            "IMAX, 4DX", 8.7, 800L, 13
        ));
        save(new Movie(null, 
            "범죄도시4", "THE ROUNDUP : PUNISHMENT", 
            "15", "2024.04.24", 
            "신종 마약 사건 3년 뒤, 괴물형사 ‘마석도’(마동석)와 서울 광수대는 배달앱을 이용한 마약 판매 사건을 수사하던 중 수배 중인 앱 개발자가 필리핀에서 사망한 사건이 대규모 온라인 불법 도박 조직과 연관되어 있음을 알아낸다. 필리핀에 거점을 두고 납치, 감금, 폭행, 살인 등으로 대한민국 온라인 불법 도박 시장을 장악한 특수부대 용병 출신의 빌런 ‘백창기’(김무열)와 한국에서 더 큰 판을 짜고 있는 IT업계 천재 CEO ‘장동철’(이동휘). ‘마석도’는 더 커진 판을 잡기 위해 ‘장이수’(박지환)에게 뜻밖의 협력을 제안하고 광역수사대는 물론, 사이버수사대까지 합류해 범죄를 소탕하기 시작하는데… 나쁜 놈 잡는데 국경도 영역도 제한 없다! 업그레이드 소탕 작전! 거침없이 싹 쓸어버린다!", 
            109, "액션, 범죄, 스릴러, 형사", 
            "허명행", "마동석,김무열,박지환, 이동휘", 
            9.8, 734L, 
            "https://i.namu.wiki/i/KwJ2dfIySu2k8JWlK3nD-gS7A9G-2I2EWKkNjoVRqaHabjK88STUo8FXi545XV6Pe8ERSX5DjF4e5k0IkOvznQ.webp",
            "https://biz.chosun.com/resizer/v2/XIXAJIUJ6YFR5YLGQDRVYHLJ54.jpg?auth=ca0a12238befb32839c719e6beedf5e71aa7684834c2bf17e3993c4530d0f706",
            // new String[] {}, 
            "IMAX", 7.6, 700L, 14
        ));
        save(new Movie(null, 
            "귀멸의칼날 무한성편", 
            "Demon Slayer: Kimetsu No Yaiba The Movie: Infinity Castle", 
            "19", "2025.08.22", 
            "혈귀로 변한 여동생 ‘네즈코’를 되돌리기 위해 귀살대가 된 ‘탄지로’! 어둠 속을 달리는 무한열차에서 승객들이 흔적 없이 사라진다는 소식에 ‘젠이츠’, ‘이노스케’와 함께 임무 수행을 위해 무한열차에 탑승한다. 그리고 그 곳에서 만난 귀살대 최강 검사 염주 ‘렌고쿠’! 이들은 무한열차에 숨어 있는 혈귀의 존재를 직감하고 모두를 구하기 위해 목숨을 건 혈전을 시작하는데… 그 칼로 악몽을 끊어라!", 
            117, "시대극 판타지, 액션", 
            "소토자키 하루오", "하나에 나츠키,키토 아카리,시모노 히로,마츠오카 요시츠구", 
            9.8, 521L, 
            "https://i.namu.wiki/i/YvPBZ1kzk8Dku4HhOC2FGB7xKVXj5bpg8cSdRWsAZg-3Knqu5LcWJchrZDIVmz-08V3OV9uFLMfCRNCZRcnTxQ.webp", 
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYEAVqhsnZC3KnuJ_JMtuHSI7WEBwFc1as3w&s", 
            // new String[] {},
            "", 6.5, 600L, 15
        ));
        save(new Movie(null, 
            "승부", "The Match", 
            "12", "2025.03.26", 
            "세계 최고 바둑 대회에서 국내 최초 우승자가 된 조훈현. 전 국민적 영웅으로 대접받던 그는 바둑 신동이라 불리는 이창호를 제자로 맞는다. “실전에선 기세가 8할이야” 제자와 한 지붕 아래에서 먹고 자며 가르친 지 수년. 모두가 스승의 뻔한 승리를 예상했던 첫 사제 대결에서 조훈현은 전 국민이 지켜보는 가운데, 기세를 탄 제자에게 충격적으로 패한다. 오랜만에 패배를 맛본 조훈현과 이제 승부의 맛을 알게 된 이창호 조훈현은 타고난 승부사적 기질을 되살리며 다시 한번 올라갈 결심을 하게 되는데…", 
            115, "드라마, 스포츠, 시대극", 
            "김형주", "이병헌,유아인", 
            9.8, 342L, 
            "https://img.megabox.co.kr/SharedImg/2025/03/27/O6RnDMOAnUw6geDdlaAXRlkqgy0mSSDb_420.jpg", 
            "https://i.namu.wiki/i/4sLX83VoaVVEDMA9Nmj9YCPnVnsnJSKIs7GEK4XSj89ofinn6FVUUjZKoIy2d5eYOgEL341pY-2Rq3PCk6c1MKAzfpVmYjfLfY0HbWjUfW1DhygfA8raXT49CaBTHWpz2gRQi49vsuXR4Y0CwInj_g.webp",
            // new String[] {}, 
            "IMAX", 5.4, 500L, 16
        ));
        save(new Movie(null, 
            "필즈 오브 데스티니", "Fields of Destiny", 
            "12", "2023.05.16", 
            "씨앗은 땅에만 뿌려지는 것이 아니다. 희망과 용기, 사랑도 이 황무지 위에서 자란다. 대공황의 그늘 아래, 미국 중부의 한 농장을 배경으로 펼쳐지는 한 가족의 세대 간 갈등과 화해, 그리고 삶의 터전을 지켜내려는 치열한 이야기. 하늘을 나는 꿈을 품은 딸, 땅을 지키려는 아버지, 이상과 현실 사이에서 갈등하는 오빠. 그들은 각자의 방식으로 ‘운명의 들판’을 가꾸며 자신만의 길을 찾아 나선다.", 
            90, "드라마, 시대극, 가족", 
            "엘리자 레이먼드", "클레어 하딩턴,루카스 멜든,윌터 그레인", 
            9.8, 5300L, 
            "https://live.staticflickr.com/65535/52900840054_14af3e493c_z.jpg", 
            "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700795880/catalog/1600659718750367744/xiry6ufbjttckqxpfzrw.jpg", 
            // new String[] {}, 
            "", 4.3, 400L, 17
        ));
        save(new Movie(null, 
            "킬러 어드바이스", "Killer Advice", 
            "19", "2021.02.05", 
            "When Beth (Kate Watson) suffers a traumatic attack, her family and friends suggest she see a therapist to help her cope. However, her new therapist gives her more than she bargained for.", 
            89, "서스팬스, 스릴러, 범죄물",
            "자레드 콘", "케이트 왓슨, 지지 거스틴,메러디스 토마스,에릭 로버츠",
            9.8, 2100L, 
            "https://m.media-amazon.com/images/M/MV5BYzgxZjY5MGQtODRhZi00ZDQyLWE5MDQtYTk3NGJkN2I5M2M1XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
            "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700796426/catalog/1600659718750367744/iqmiudmmo6s7zcofwmpf.jpg",
            // new String[] {},
            "4DX", 3.2, 300L, 18
        ));
        save(new Movie(null, 
            "인터스텔라", "InterStella", 
            "12", "2014.11.06", 
            "세계 각국의 정부와 경제가 완전히 붕괴된 미래가 다가온다. 지난 20세기에 범한 잘못이 전 세계적인 식량 부족을 불러왔고, NASA도 해체되었다. 이때 시공간에 불가사의한 틈이 열리고, 남은 자들에게는 이 곳을 탐험해 인류를 구해야 하는 임무가 주어진다. 사랑하는 가족들을 뒤로 한 채 인류라는 더 큰 가족을 위해, 그들은 이제 희망을 찾아 우주로 간다. 그리고 우린 답을 찾을 것이다. 늘 그랬듯이...",
            169, "SF, 모험, 재난, 가족", 
            "크리스토퍼 놀란", "매튜 매커너히,앤 해서웨이,제시카 차스테인,마이클 케인",
            9.8, 1500L, 
            "https://rukminim2.flixcart.com/image/850/1000/l2dmky80/poster/y/f/b/small-poster-interstellar-sl407-wall-poster-13x19-inches-matte-original-imagdqezkfchjkhz.jpeg?q=20&crop=false", 
            "https://i.ytimg.com/vi/YF1eYbfbH5k/maxresdefault.jpg?sqp=-oaymwEmCIAKENAF8quKqQMa8AEB-AH-CYAC0AWKAgwIABABGE4gYShlMA8=&rs=AOn4CLDt6JPoesvmQnP8qf-00JpeDZUfyA",
            // new String[] {}, 
            "IMAX", 2.3, 200L, 19
        ));
        save(new Movie(null, 
            "내 이름은 알프레드 히치콕", "My Name is Alfred Hitchcock", 
            "ALL", "2022.09.05", 
            "내 이름은 알프레드 히치콕(My Name Is Alfred Hitchcock)은 마크 커즌스(Mark Cousins)가 각본과 감독을 맡은 2022년 영국 다큐멘터리 영화입니다. 영국의 영화감독 알프레드 히치콕에 관한 이야기이다", 
            120, "다큐멘터리", 
            "마크 코신스", "알리스테어 맥고완", 
            9.8, 1300L, 
            "https://www.mvtimes.com/mvt/uploads/2024/11/film-my-name-alfred-hitchcock-2.jpg", 
            "https://theknockturnal.com/wp-content/uploads/2024/11/maxresdefault-3.jpg", 
            // new String[] {}, 
            "", 1.2, 100L, 20
        ));
    }
}
