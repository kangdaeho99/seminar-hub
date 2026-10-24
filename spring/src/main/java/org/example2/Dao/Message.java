package org.example2.Dao;


public class Message {
    String text;

    private Message(String text){ //생성자가 private으로 선언되어 있어서 외부에서 생성자를 통해 오브젝트를 만들 수 없다.
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public static Message newMessage(String text){ // 생성자 대신 사용할 수 있는 스태틱 팩토리 메소드를 사용한다.
        return new Message(text);
    }
}
