package com.example.demo.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Annotations.Auth;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
public class MovielikeuserController {

    @Autowired
    DAOUser daoUser;

    @GetMapping("/movieLikeToggle/{movieid}")
    public List<Long> deleteReview(@PathVariable Long movieid,@Auth User user){

        List<Long> likedMovies=user.getLikemovies();

        if(likedMovies.contains(movieid))/* 이미 영화를 라이크했던 경우 -> 라이크 제거 */
            likedMovies.remove(movieid);
        else/* 영화를 기존에 라이크하지 않았던 경우 -> 라이크 추가 */
            likedMovies.add(movieid);

        user.setLikemovies(likedMovies);
        daoUser.Modify(user);
        return likedMovies;
    }

}
