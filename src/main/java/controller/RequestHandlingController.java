package controller;

import exception.BadRequestException;
import model.*;
import service.ResponseService;
import utils.LogLevel;
import utils.Logger;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static controller.RequestPatternType.*;

public class RequestHandlingController {
    private final Logger logger;
    private ResponseService responseService;

    public RequestHandlingController(Logger logger) {
        this.logger = logger;
        responseService = new ResponseService(logger);
    }

    public String handleRequest(String request) {
        try {
            Matcher matcher;
            if ((matcher = SignUp.getPattern().matcher(request)).find())
                return signUp(matcher).convertToJson();
            else if ((matcher = Login.getPattern().matcher(request)).find())
                return login(matcher).convertToJson();
            else if ((matcher = SendTweet.getPattern().matcher(request)).find())
                return sendTweet(matcher).convertToJson();
            else if ((matcher = RefreshTimeLine.getPattern().matcher(request)).find())
                return refreshTimeLine(matcher).convertToJson();
            else if ((matcher = LikeTweet.getPattern().matcher(request)).find())
                return likeTweet(matcher).convertToJson();
            else if ((matcher = CommentTweet.getPattern().matcher(request)).find())
                return commentTweet(matcher).convertToJson();
            else if ((matcher = SearchUser.getPattern().matcher(request)).find())
                return searchUser(matcher).convertToJson();
            else if ((matcher = Follow.getPattern().matcher(request)).find())
                return follow(matcher).convertToJson();
            else if ((matcher = UnFollow.getPattern().matcher(request)).find())
                return unFollow(matcher).convertToJson();
            else if ((matcher = Profile.getPattern().matcher(request)).find())
                return getProfile(matcher).convertToJson();
            else if ((matcher = SetBio.getPattern().matcher(request)).find())
                return setBio(matcher).convertToJson();
            else if ((matcher = ChangePassword.getPattern().matcher(request)).find())
                return changePassword(matcher).convertToJson();
            else if ((matcher = Logout.getPattern().matcher(request)).find())
                return logout(matcher).convertToJson();
            else
                throw new BadRequestException("Bad request format.");
        } catch (BadRequestException e) {
            logger.log(LogLevel.Error, e.getMessage());
            return new Response<>(ResponseType.Error, e.getMessage()).convertToJson();
        }
    }

    private Response<String> signUp(Matcher matcher) {
        responseService.signUp(matcher.group(1), matcher.group(2));
        return new Response<>(ResponseType.Successful, "");
    }

    private Response<String> login(Matcher matcher) {
        String token = responseService.login(matcher.group(1), matcher.group(2));
        return new Response<>(ResponseType.Token, token);
    }

    private Response<String> sendTweet(Matcher matcher) {
        responseService.sendTweet(matcher.group(1), matcher.group(2));
        return new Response<>(ResponseType.Successful, "Tweet is sent successfully.");
    }

    private Response<List<Tweet>> refreshTimeLine(Matcher matcher) {
        List<Tweet> tweets = responseService.refreshTimeLine(matcher.group(1));
        return new Response<>(ResponseType.List, tweets);
    }

    private Response<String> likeTweet(Matcher matcher) {
        responseService.likeTweet(matcher.group(1), Integer.parseInt(matcher.group(2)));
        return new Response<>(ResponseType.Successful, "Tweet " + matcher.group(2) + " successfully liked.");
    }

    private Response<String> commentTweet(Matcher matcher) {
        responseService.commentTweet(matcher.group(1), Integer.parseInt(matcher.group(2)), matcher.group(3));
        return new Response<>(ResponseType.Successful, "Comment is sent successfully.");
    }

    private Response<Profile> searchUser(Matcher matcher) {
        model.Profile profile = responseService.search(matcher.group(1), matcher.group(2));
        return new Response<>(ResponseType.Profile, profile);
    }

    private Response<String> follow(Matcher matcher) {
        responseService.follow(matcher.group(1), matcher.group(2));
        return new Response<>(ResponseType.Successful, "User " + matcher.group(2) + " is followed successfully.");
    }

    private Response<String> unFollow(Matcher matcher) {
        responseService.unFollow(matcher.group(1), matcher.group(2));
        return new Response<>(ResponseType.Successful, "User " + matcher.group(2) + " is unfollowed successfully.");
    }

    private Response<Profile> getProfile(Matcher matcher) {
        model.Profile profile = responseService.getProfile(matcher.group(1));
        return new Response<>(ResponseType.Profile, profile);
    }

    private Response<String> setBio(Matcher matcher) {
        responseService.setBio(matcher.group(1), matcher.group(2));
        return new Response<>(ResponseType.Successful, "Bio is updated successfully.");
    }

    private Response<String> changePassword(Matcher matcher) {
        responseService.changePassword(matcher.group(1), matcher.group(2), matcher.group(3));
        return new Response<>(ResponseType.Successful, "Password is changed successfully.");
    }

    private Response<String> logout(Matcher matcher) {
        responseService.logout(matcher.group(1));
        return new Response<>(ResponseType.Successful, "");
    }

    private Response<Map<String, String>> viewCommentsOfTweet(Matcher matcher) {
        Map<String, String> comments = responseService.getComments(matcher.group(1), Integer.parseInt(matcher.group(2)));
        return new Response<>(ResponseType.Map, comments);
    }

    private Response<Integer> viewLikesOfTweet(Matcher matcher) {
        int likes = responseService.getLikes(matcher.group(1), Integer.parseInt(matcher.group(2)));
        return new Response<>(ResponseType.Successful, likes);
    }

    private Response<List<TweetView>> refreshTimeLineView(Matcher matcher) {
        List<TweetView> tweets = responseService.refreshTimeLineTweetView(matcher.group(1));
        return new Response<>(ResponseType.List, tweets);
    }
}
