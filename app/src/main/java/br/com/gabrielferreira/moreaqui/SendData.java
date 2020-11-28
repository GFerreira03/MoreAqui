package br.com.gabrielferreira.moreaqui;

import java.io.Serializable;

public class SendData implements Command{

    private Long key;
    private Serializable value;

    public long getKey() { return key; }
    public void setKey(Long key) { this.key = key; }

    public Serializable getValue() { return value; }
    public void setValue(Serializable value) { this.value = value; }

    @Override
    public void execute(DaoImpl d) { d.add(key, value); }
}
