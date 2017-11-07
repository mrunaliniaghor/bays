/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spam.entity;

import java.io.Serializable;

/**
 *
 * @author Girase
 */
public class Mail implements Serializable {

    String from;
    String to;
    String subject;
    String sent;
    String content;
    float pSpam;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public float getpSpam() {
        return pSpam;
    }

    public void setpSpam(float pSpam) {
        this.pSpam = pSpam;
    }

    @Override
    public String toString() {
        return "Mail{" + "from=" + from + ", to=" + to + ", subject=" + subject + ", sent=" + sent + ", content=" + content + ", pSpam=" + pSpam + '}';
    }
}
