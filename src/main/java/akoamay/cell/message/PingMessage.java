package akoamay.cell.message;

import lombok.Data;

public class PingMessage extends Message {
    public void PingMesssge() {
        setType(0);
        setMessage("Are you there?");
    }
}