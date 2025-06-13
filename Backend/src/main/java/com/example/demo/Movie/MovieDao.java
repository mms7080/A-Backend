package com.example.demo.Movie;

import java.time.LocalDateTime;
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
        return repo.findAllByOrderByIdAsc();
    }

    public Movie findById(Long id) {
        return repo.findFirstById(id);
    }

    public void save(Movie movie) {
        repo.save(movie);
    }

    public boolean existsById(Long id) {
        return repo.existsById(id);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }


    public List<Movie> findForMoviePage() {
        return repo.findIdAndRankAndDescriptionAndScoreAndTitleAndRateAndReleaseDateAndLikeNumberAndPosterAndLabelAndReserveRateByOrderByIdAsc();
    }



    // 초기 데이터 입력
    @PostConstruct
    @Transactional
    public void insertTestMovies() {
        if(repo.count() > 0) return;
        LocalDateTime now = LocalDateTime.now();
        
        save(new Movie(null, 
            "필즈 오브 데스티니", "Fields of Destiny", 
            "12", "2023.05.16", 
            "씨앗은 땅에만 뿌려지는 것이 아니다. 희망과 용기, 사랑도 이 황무지 위에서 자란다. 대공황의 그늘 아래, 미국 중부의 한 농장을 배경으로 펼쳐지는 한 가족의 세대 간 갈등과 화해, 그리고 삶의 터전을 지켜내려는 치열한 이야기. 하늘을 나는 꿈을 품은 딸, 땅을 지키려는 아버지, 이상과 현실 사이에서 갈등하는 오빠. 그들은 각자의 방식으로 ‘운명의 들판’을 가꾸며 자신만의 길을 찾아 나선다.", 
            90, "드라마, 시대극, 가족", 
            "엘리자 레이먼드", "클레어 하딩턴,루카스 멜든,윌터 그레인", 
            9.8, 5300L, 
            "/images/poster1.jpg", 
            "/images/wide1.jpg",
            List.of(),
            "https://www.youtube.com/embed/2cLHNgN5PKY", 
            "", 20.1, 2000L, 1, now
        ));
        save(new Movie(null, 
            "킬러 어드바이스", "Killer Advice", 
            "19", "2021.02.05", 
            "When Beth (Kate Watson) suffers a traumatic attack, her family and friends suggest she see a therapist to help her cope. However, her new therapist gives her more than she bargained for.", 
            89, "서스팬스, 스릴러, 범죄물",
            "자레드 콘", "케이트 왓슨, 지지 거스틴,메러디스 토마스,에릭 로버츠",
            9.8, 2100L, 
            "/images/poster2.jpg",
            "/images/wide2.jpg",
            List.of("/images/stillCut2-1.jpg","/images/stillCut2-2.jpg"),
            "https://www.youtube.com/embed/g_yDsQDJOjg",
            "4DX", 19.8, 1900L, 2, now
        ));
        save(new Movie(null, 
            "인터스텔라", "InterStella", 
            "12", "2014.11.06", 
            "세계 각국의 정부와 경제가 완전히 붕괴된 미래가 다가온다. 지난 20세기에 범한 잘못이 전 세계적인 식량 부족을 불러왔고, NASA도 해체되었다. 이때 시공간에 불가사의한 틈이 열리고, 남은 자들에게는 이 곳을 탐험해 인류를 구해야 하는 임무가 주어진다. 사랑하는 가족들을 뒤로 한 채 인류라는 더 큰 가족을 위해, 그들은 이제 희망을 찾아 우주로 간다. 그리고 우린 답을 찾을 것이다. 늘 그랬듯이...",
            169, "SF, 모험, 재난, 가족", 
            "크리스토퍼 놀란", "매튜 매커너히,앤 해서웨이,제시카 차스테인,마이클 케인",
            9.8, 1500L, 
            "/images/poster3.jpg", 
            "/images/wide3.jpg",
            List.of("/images/stillCut3-1.jpg","/images/stillCut3-2.jpg"),
            "https://www.youtube.com/embed/d2VN6NNa9BE", 
            "IMAX", 18.7, 1800L, 3, now
        ));
        save(new Movie(null, 
            "내 이름은 알프레드 히치콕", "My Name is Alfred Hitchcock", 
            "ALL", "2022.09.05", 
            "내 이름은 알프레드 히치콕(My Name Is Alfred Hitchcock)은 마크 커즌스(Mark Cousins)가 각본과 감독을 맡은 2022년 영국 다큐멘터리 영화입니다. 영국의 영화감독 알프레드 히치콕에 관한 이야기이다", 
            120, "다큐멘터리", 
            "마크 코신스", "알리스테어 맥고완", 
            9.8, 1300L, 
            "/images/poster4.jpg", 
            "/images/wide4.jpg",
            List.of("/images/stillCut4-1.jpg","/images/stillCut4-2.jpg"),
            "https://www.youtube.com/embed/xpvBJXhsUDU", 
            "", 17.6, 1700L, 4, now
        ));
        save(
            new Movie(null, 
            "어벤져스: 엔드게임", "Avengers: Endgame", 
            "15", "2019.04.24", 
            "인피니티 워 이후 많은 사람이 죽고 또 많은 것을 잃게 된 지구는 더 이상 희망이 남지 않아 절망 속에 살아간다. 전쟁 후 남아 있던 어벤저스는 그런 그들의 모습을 보게 된다. 마지막으로 지구를 살리려 모든 것을 건 타노스와 최후의 전쟁을 치른다.", 
            181, "슈퍼히어로, SF, 액션, 판타지", 
            "앤서니 루소, 조 루소", "로버트 다우니 주니어,크리스 에반스,마크 러팔로,크리스 헴스워스,스칼렛 요한슨,제레미 레너", 
            9.8, 986L, 
            "/images/poster5.jpg", 
            "/images/wide5.jpg",
            List.of("/images/stillCut5-1.jpg","/images/stillCut5-2.jpg"),
            "https://www.youtube.com/embed/Ko2NWhXI9e8", 
            "IMAX, 4DX", 16.5, 1600L, 5, now
        ));
        save(new Movie(null, 
            "범죄도시4", "THE ROUNDUP : PUNISHMENT", 
            "15", "2024.04.24", 
            "신종 마약 사건 3년 뒤, 괴물형사 ‘마석도’(마동석)와 서울 광수대는 배달앱을 이용한 마약 판매 사건을 수사하던 중 수배 중인 앱 개발자가 필리핀에서 사망한 사건이 대규모 온라인 불법 도박 조직과 연관되어 있음을 알아낸다. 필리핀에 거점을 두고 납치, 감금, 폭행, 살인 등으로 대한민국 온라인 불법 도박 시장을 장악한 특수부대 용병 출신의 빌런 ‘백창기’(김무열)와 한국에서 더 큰 판을 짜고 있는 IT업계 천재 CEO ‘장동철’(이동휘). ‘마석도’는 더 커진 판을 잡기 위해 ‘장이수’(박지환)에게 뜻밖의 협력을 제안하고 광역수사대는 물론, 사이버수사대까지 합류해 범죄를 소탕하기 시작하는데… 나쁜 놈 잡는데 국경도 영역도 제한 없다! 업그레이드 소탕 작전! 거침없이 싹 쓸어버린다!", 
            109, "액션, 범죄, 스릴러, 형사", 
            "허명행", "마동석,김무열,박지환, 이동휘", 
            9.8, 734L, 
            "/images/poster6.jpg",
            "/images/wide6.jpg",
            List.of("/images/stillCut6-1.jpg","/images/stillCut6-2.jpg"),
            "https://www.youtube.com/embed/pMAPj6WVsT4", 
            "IMAX", 15.4, 1500L, 6, now
        ));
        save(new Movie(null, 
            "귀멸의칼날 무한성편", 
            "Demon Slayer: Kimetsu No Yaiba The Movie: Infinity Castle", 
            "19", "2100.08.22", 
            "혈귀로 변한 여동생 ‘네즈코’를 되돌리기 위해 귀살대가 된 ‘탄지로’! 어둠 속을 달리는 무한열차에서 승객들이 흔적 없이 사라진다는 소식에 ‘젠이츠’, ‘이노스케’와 함께 임무 수행을 위해 무한열차에 탑승한다. 그리고 그 곳에서 만난 귀살대 최강 검사 염주 ‘렌고쿠’! 이들은 무한열차에 숨어 있는 혈귀의 존재를 직감하고 모두를 구하기 위해 목숨을 건 혈전을 시작하는데… 그 칼로 악몽을 끊어라!", 
            117, "시대극 판타지, 액션", 
            "소토자키 하루오", "하나에 나츠키,키토 아카리,시모노 히로,마츠오카 요시츠구", 
            9.8, 521L, 
            "/images/poster7.jpg", 
            "/images/wide7.jpg",
            List.of("/images/stillCut7-1.jpg","/images/stillCut7-2.jpg"),
            "https://www.youtube.com/embed/dndBpb41Q-w",
            "", 14.3, 1400L, 7, now
        ));
        save(new Movie(null, 
            "승부", "The Match", 
            "12", "2025.03.26", 
            "세계 최고 바둑 대회에서 국내 최초 우승자가 된 조훈현. 전 국민적 영웅으로 대접받던 그는 바둑 신동이라 불리는 이창호를 제자로 맞는다. “실전에선 기세가 8할이야” 제자와 한 지붕 아래에서 먹고 자며 가르친 지 수년. 모두가 스승의 뻔한 승리를 예상했던 첫 사제 대결에서 조훈현은 전 국민이 지켜보는 가운데, 기세를 탄 제자에게 충격적으로 패한다. 오랜만에 패배를 맛본 조훈현과 이제 승부의 맛을 알게 된 이창호 조훈현은 타고난 승부사적 기질을 되살리며 다시 한번 올라갈 결심을 하게 되는데…", 
            115, "드라마, 스포츠, 시대극", 
            "김형주", "이병헌,유아인", 
            9.8, 342L, 
            "/images/poster8.jpg", 
            "/images/wide8.jpg",
            List.of("/images/stillCut8-1.jpg","/images/stillCut8-2.jpg"),
            "https://www.youtube.com/embed/J8qqMLZPPTo", 
            "IMAX", 13.2, 1300L, 8, now
        ));

        
        save(new Movie(null, 
            "28년 후", "28 Years Later", 
            "19", "2025.06.19", 
            "태어나 처음 마주한 바이러스에 감염된 세상,충격을 넘어선 극강의 공포가 밀려온다! 28년 전 생물학 무기 연구소에서 세상을 재앙으로 몰아넣은 바이러스가 유출된 후, 일부 생존자들이 모여 철저히 격리된 채 살아가는 섬 ‘홀리 아일랜드", 
            115, "공포(호러), 드라마, 스릴러", 
            "대니 보일", "조디 코머, 애런 존슨, 랄프 파인즈, 잭 오코넬, 알피 윌리엄스", 
            9.8, 5300L, 
            "/images/poster9.jpg", 
            "/images/wide9.jpg",
            List.of("/images/stillCut9-1.jpg","/images/stillCut9-2.jpg"),
            "https://www.youtube.com/embed/iSyYE__waY4", 
            "IMAX", 20.2, 1200L, 9, now
        ));
        save(new Movie(null, 
            "드래곤 길들이기", "How to Train Your Dragon", 
            "ALL", "2025.06.06", 
            "드래곤을 없애는 것이 삶의 모든 목적인 바이킹들과 다른 신념을 가진 ‘히컵’은 무리 속에 속하지 못하고 족장인 아버지에게도 인정받지 못한다.", 
            125 , "액션, 어드벤처, 판타지",
            "딘 데블로이스", " 메이슨 테임즈, 제라드 버틀러, 니코 파커, 닉 프로스트, 줄리안 데니슨, 가브리엘 하웰, 브론윈 제임스, 해리 트레발드윈",
            9.8, 2100L, 
            "/images/poster10.jpg",
            "/images/wide10.jpg",
            List.of("/images/stillCut10-1.jpg","/images/stillCut10-2.jpg"),
            "https://www.youtube.com/embed/eyeXAgO7rp8",
            "4DX", 17.3, 1100L, 10, now
        ));
        save(new Movie(null, 
            "F1 더 무비", "F1: The Movie", 
            "12", "2025.6.25", 
            "최고가 되지 못한 전설 VS 최고가 되고 싶은 루키 한때 주목받는 유망주였지만 끔찍한 사고로 F1®에서 우승하지 못하고 한순간에 추락한 드라이버 '소니 헤이스'(브래드 피트).",
            155, "드라마, 액션", 
            "조셉 코신스키", "브래드 피트, 댐슨 이드리스, 케리 콘돈, 하비에르 바르뎀",
            9.8, 1500L, 
            "/images/poster11.jpg", 
            "/images/wide11.jpg",
            List.of("/images/stillCut11-1.jpg","/images/stillCut11-2.jpg"),
            "https://www.youtube.com/embed/d2VN6NNa9BE", 
            "IMAX", 18.9, 1000L, 11, now
        ));
        save(new Movie(null, 
            "하이파이브", "HI-FIVE", 
            "15", "2025.05.30", 
            "평범과 비범 사이! 우리가 누구? ‘하이파이브’ 태권소녀 ‘완서’, 작가 지망생 ‘지성’, 프레시 매니저 ‘선녀’, FM 작업반장 ‘약선’ 그리고 힙스터 백수 ‘기동’.", 
            119, "액션, 코미디, 판타지", 
            "강형철", "이재인, 안재홍, 라미란, 김희원, 유아인, 오정세, 박진영", 
            9.8, 1300L, 
            "/images/poster12.jpg", 
            "/images/wide12.jpg",
            List.of("/images/stillCut12-1.jpg","/images/stillCut12-2.jpg"),
            "https://www.youtube.com/embed/MhBgXdJuYLg", 
            "", 19.9, 900L, 12, now
        ));
    }
}
