package fr.republicraft.velocity.votes.serveurprive;

import fr.republicraft.common.api.helper.JsonHelper;
import fr.republicraft.common.api.net.LightHTTPClient;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class ServeurPriveVote implements Vote {

    String url;
    String token;
    String username;
    String userIP;
    private ServeurPriveVoteResult result;

    public LightHTTPClient getClient() {
        return new LightHTTPClient();
    }

    @Override
    public boolean vote() {
        String fullUrl = url + "/" + token + "/" + userIP;
        System.out.println("vote to url=" + fullUrl);
        String output = getClient().get(fullUrl);
        result = JsonHelper.fromJson(output, ServeurPriveVoteResult.class);
        return result.getStatus().getStatus() == ServeurPriveVoteResult.Status.OK.getStatus();
    }
}
