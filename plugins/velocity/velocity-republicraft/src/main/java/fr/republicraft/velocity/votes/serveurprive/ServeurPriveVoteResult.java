package fr.republicraft.velocity.votes.serveurprive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServeurPriveVoteResult {

    Status status;

    /**
     * Correspond à la date du vote au format timestamp
     */
    int vote;
    /**
     * Correspond au nombre de secondes restantes avant que l'utilisateur puisse à nouveau voter
     */
    int nextvote;
    /**
     * Pseudonyme de l'utilisateur (si il a spécifié son pseudo lors de son vote)
     */
    String pseudo;

    public enum Status {
        KO(0),
        OK(1);

        private int status;

        //Constructeur
        Status(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

}
