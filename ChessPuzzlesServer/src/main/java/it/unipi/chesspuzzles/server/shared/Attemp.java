package it.unipi.chesspuzzles.server.shared;

import it.unipi.chesspuzzles.server.database.DatabaseAttemp;

@SuppressWarnings("unused")
public class Attemp {
    public Integer id;
    public Integer userId;
    public String timestamp;
    public TYPE type;

    public Attemp(DatabaseAttemp dbAttemp) {
        this.id = dbAttemp.getId();
        this.userId = dbAttemp.getUserId();
        this.timestamp = dbAttemp.getTimestamp().toString();
        this.type = TYPE.valueOf(dbAttemp.getType());
    }

    // Default constructor is required by gson
    public Attemp() {}

    public enum TYPE {
        SOLVE,
        HINT,
        FAIL
    }
}
