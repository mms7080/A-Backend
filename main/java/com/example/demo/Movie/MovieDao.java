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

    @PostConstruct
    @Transactional
    public void insertTestMovies() {
        if(repo.count() > 0) return;
        save(new Movie(null, 1, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "Fields of Destiny", "12", "2023.05.16", 5300L, "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700795880/catalog/1600659718750367744/xiry6ufbjttckqxpfzrw.jpg"));
        save(new Movie(null, 2, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "Killer Advice", "19", "2021.02.05", 2100L, "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700796426/catalog/1600659718750367744/iqmiudmmo6s7zcofwmpf.jpg"));
        save(new Movie(null, 3, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "InterStella", "12", "2014.11.06", 1500L, "https://rukminim2.flixcart.com/image/850/1000/l2dmky80/poster/y/f/b/small-poster-interstellar-sl407-wall-poster-13x19-inches-matte-original-imagdqezkfchjkhz.jpeg?q=20&crop=false"));
        save(new Movie(null, 4, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "My Name is Alfred Hitchcock", "ALL", "2022.09.05", 1300L, "https://www.mvtimes.com/mvt/uploads/2024/11/film-my-name-alfred-hitchcock-2.jpg"));
        save(new Movie(null, 5, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, "MEGA ONLY", "어벤져스 엔드게임", "15", "2019.04.24", 986L, "https://upload.wikimedia.org/wikipedia/ko/thumb/f/f2/%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4-_%EC%97%94%EB%93%9C%EA%B2%8C%EC%9E%84_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg/1200px-%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4-_%EC%97%94%EB%93%9C%EA%B2%8C%EC%9E%84_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg"));
        save(new Movie(null, 6, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "범죄도시 4", "15", "2024.04.24", 734L, "https://i.namu.wiki/i/KwJ2dfIySu2k8JWlK3nD-gS7A9G-2I2EWKkNjoVRqaHabjK88STUo8FXi545XV6Pe8ERSX5DjF4e5k0IkOvznQ.webp"));
        save(new Movie(null, 7, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "귀멸의칼날 무한성편", "19", "2025.08.22", 521L, "https://i.namu.wiki/i/YvPBZ1kzk8Dku4HhOC2FGB7xKVXj5bpg8cSdRWsAZg-3Knqu5LcWJchrZDIVmz-08V3OV9uFLMfCRNCZRcnTxQ.webp"));
        save(new Movie(null, 8, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, "Dolby", "승부", "12", "2025.03.26", 342L, "https://img.megabox.co.kr/SharedImg/2025/03/27/O6RnDMOAnUw6geDdlaAXRlkqgy0mSSDb_420.jpg"));
        save(new Movie(null, 9, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "Fields of Destiny", "12", "2023.05.16", 5300L, "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700795880/catalog/1600659718750367744/xiry6ufbjttckqxpfzrw.jpg"));
        save(new Movie(null, 10, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "Killer Advice", "19", "2021.02.05", 2100L, "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700796426/catalog/1600659718750367744/iqmiudmmo6s7zcofwmpf.jpg"));
        save(new Movie(null, 11, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "InterStella", "12", "2014.11.06", 1500L, "https://rukminim2.flixcart.com/image/850/1000/l2dmky80/poster/y/f/b/small-poster-interstellar-sl407-wall-poster-13x19-inches-matte-original-imagdqezkfchjkhz.jpeg?q=20&crop=false"));
        save(new Movie(null, 12, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "My Name is Alfred Hitchcock", "ALL", "2022.09.05", 1300L, "https://www.mvtimes.com/mvt/uploads/2024/11/film-my-name-alfred-hitchcock-2.jpg"));
        save(new Movie(null, 13, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, "MEGA ONLY", "어벤져스 엔드게임", "15", "2019.04.24", 986L, "https://upload.wikimedia.org/wikipedia/ko/thumb/f/f2/%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4-_%EC%97%94%EB%93%9C%EA%B2%8C%EC%9E%84_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg/1200px-%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4-_%EC%97%94%EB%93%9C%EA%B2%8C%EC%9E%84_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg"));
        save(new Movie(null, 14, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "범죄도시 4", "15", "2024.04.24", 734L, "https://i.namu.wiki/i/KwJ2dfIySu2k8JWlK3nD-gS7A9G-2I2EWKkNjoVRqaHabjK88STUo8FXi545XV6Pe8ERSX5DjF4e5k0IkOvznQ.webp"));
        save(new Movie(null, 15, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "귀멸의칼날 무한성편", "19", "2025.08.22", 521L, "https://i.namu.wiki/i/YvPBZ1kzk8Dku4HhOC2FGB7xKVXj5bpg8cSdRWsAZg-3Knqu5LcWJchrZDIVmz-08V3OV9uFLMfCRNCZRcnTxQ.webp"));
        save(new Movie(null, 16, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, "Dolby", "승부", "12", "2025.03.26", 342L, "https://img.megabox.co.kr/SharedImg/2025/03/27/O6RnDMOAnUw6geDdlaAXRlkqgy0mSSDb_420.jpg"));
        save(new Movie(null, 17, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "Fields of Destiny", "12", "2023.05.16", 5300L, "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700795880/catalog/1600659718750367744/xiry6ufbjttckqxpfzrw.jpg"));
        save(new Movie(null, 18, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "Killer Advice", "19", "2021.02.05", 2100L, "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700796426/catalog/1600659718750367744/iqmiudmmo6s7zcofwmpf.jpg"));
        save(new Movie(null, 19, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "InterStella", "12", "2014.11.06", 1500L, "https://rukminim2.flixcart.com/image/850/1000/l2dmky80/poster/y/f/b/small-poster-interstellar-sl407-wall-poster-13x19-inches-matte-original-imagdqezkfchjkhz.jpeg?q=20&crop=false"));
        save(new Movie(null, 20, "타노스 위협으로부터 몸을 지키기 위해 거대한 벽을 쌓고 그 안에서...", 9.8, null, "My Name is Alfred Hitchcock", "ALL", "2022.09.05", 1300L, "https://www.mvtimes.com/mvt/uploads/2024/11/film-my-name-alfred-hitchcock-2.jpg"));
    }
}
