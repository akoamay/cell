package akoamay.cell.message;

import java.io.Serializable;

import lombok.Data;

@Data
public class Message implements Serializable{
    private int type;
    private String message;
    private byte[] data;
}