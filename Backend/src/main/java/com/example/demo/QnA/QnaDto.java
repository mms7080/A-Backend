package com.example.demo.QnA;

public class QnaDto {
    private Qna qna;

    public Long getId(){return qna.getId();}
    public String getAuthor(){return qna.getAuthor();}
    public String getReplyto(){return qna.getReplyto();}
    public Long getReplytoid(){return qna.getReplytoid();}
    public String getTitle(){return qna.getTitle();}
    public String getContent(){return qna.getContent();}
    public String getWritetime(){return qna.getWritetime();}

    public void setId(Long id){qna.setId(id);}
    public void setAuthor(String author){qna.setAuthor(author);}
    public void setReplyto(String replyto){qna.setReplyto(replyto);}
    public void setReplytoid(Long replytoid){qna.setReplytoid(replytoid);}
    public void setTitle(String title){qna.setTitle(title);}
    public void setContent(String content){qna.setContent(content);}
    public void setWritetime(String writetime){qna.setWritetime(writetime);}    
}
