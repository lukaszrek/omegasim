package org.anomek.omegasim.scenarios.partysynergy;

import com.badlogic.gdx.graphics.Texture;

import java.util.List;

import static java.util.Arrays.asList;

public class Textures {
    Texture m;
    Texture f;
    Texture shield;
    Texture blades;
    Texture arena;
    Texture eye;
    Texture stack;

    Texture far;
    Texture mid;

    Texture smn;
    Texture dnc;
    Texture war;
    Texture gnb;
    Texture sam;
    Texture drg;
    Texture sch;
    Texture whm;

    Texture psX;
    Texture psO;
    Texture psS;
    Texture psT;

    Texture w1;
    Texture w2;
    Texture w3;
    Texture w4;
    Texture wA;
    Texture wB;
    Texture wC;
    Texture wD;


    Textures() {
        m = new Texture("partysynergy/m.png");
        f = new Texture("partysynergy/f.png");
        shield = new Texture("partysynergy/mshield.png");
        blades = new Texture("partysynergy/fsword.png");
        arena = new Texture("partysynergy/arena.png");
        eye = new Texture("partysynergy/eye.png");
        stack = new Texture("partysynergy/stack.png");


        far = new Texture("partysynergy/far.png");
        mid = new Texture("partysynergy/mid.png");

        smn = new Texture("partysynergy/SMN.png");
        dnc = new Texture("partysynergy/DNC.png");
        war = new Texture("partysynergy/WAR.png");
        gnb = new Texture("partysynergy/GNB.png");
        sam = new Texture("partysynergy/SAM.png");
        drg = new Texture("partysynergy/DRG.png");
        sch = new Texture("partysynergy/SCH.png");
        whm = new Texture("partysynergy/WHM.png");


        psX = new Texture("partysynergy/psX.png");
        psO = new Texture("partysynergy/psO.png");
        psS = new Texture("partysynergy/psS.png");
        psT = new Texture("partysynergy/psT.png");

        w1 = new Texture("partysynergy/waymark1.png");
        w2 = new Texture("partysynergy/waymark2.png");
        w3 = new Texture("partysynergy/waymark3.png");
        w4 = new Texture("partysynergy/waymark4.png");
        wA = new Texture("partysynergy/waymarkA.png");
        wB = new Texture("partysynergy/waymarkB.png");
        wC = new Texture("partysynergy/waymarkC.png");
        wD = new Texture("partysynergy/waymarkD.png");
    }

    List<Texture> waymarks() {
        return asList(wA, w1, wB, w2, wC, w3, wD, w4);
    }

    List<Texture> jobs() {
        return asList(whm, smn, drg, war, gnb, sam, dnc, sch);
    }

    List<Texture> playstation() {
        return asList(psX, psO, psS, psT);
    }
}
