package Translate;

import Frame.Frame;
import Tree.Stm;

import java.util.List;

public class ProcFrag {
    public List<Stm> body;
    public Frame frame;

    public ProcFrag(List<Stm> body, Frame frame) {
        this.body = body;
        this.frame = frame;
    }
}