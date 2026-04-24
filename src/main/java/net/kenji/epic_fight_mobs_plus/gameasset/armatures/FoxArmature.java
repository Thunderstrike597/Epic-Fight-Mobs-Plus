package net.kenji.epic_fight_mobs_plus.gameasset.armatures;

import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.model.Armature;

import java.util.Map;

public class FoxArmature extends Armature {
    public final Joint torso;
    public final Joint chest;
    public final Joint chestUpper;
    public final Joint head;
    public final Joint snout;
    public final Joint jaw;
    public final Joint earR;
    public final Joint earL;
    public final Joint earRUpper;
    public final Joint earLUpper;
    public final Joint eyeR;
    public final Joint eyeL;
    // Front legs
    public final Joint shoulderFR;
    public final Joint thighFR;
    public final Joint legFR;
    public final Joint pawFR;
    public final Joint shoulderFL;
    public final Joint thighFL;
    public final Joint legFL;
    public final Joint pawFL;

    // Rear legs
    public final Joint shoulderRR;
    public final Joint thighRR;
    public final Joint legRR;
    public final Joint pawRR;
    public final Joint shoulderRL;
    public final Joint thighRL;
    public final Joint legRL;
    public final Joint pawRL;
    // Tail
    public final Joint tail_0;
    public final Joint tail_1;
    public final Joint tail_2;
    public final Joint tail_3;
    public final Joint tail_4;
    public FoxArmature(String name, int jointNumber, Joint rootJoint, Map<String, Joint> jointMap) {
        super(name, jointNumber, rootJoint, jointMap);

        // Core body
        this.torso = getOrLogException(jointMap, "Torso");
        this.chest = getOrLogException(jointMap, "Chest");
        this.chestUpper = getOrLogException(jointMap, "Chest_Upper");

        // Head
        this.head = getOrLogException(jointMap, "Head");
        this.snout = getOrLogException(jointMap, "Snout");
        this.jaw = getOrLogException(jointMap, "Jaw");
        this.earR = getOrLogException(jointMap, "Ear_R");
        this.earL = getOrLogException(jointMap, "Ear_L");
        this.earRUpper = getOrLogException(jointMap, "Ear_R_Upper");
        this.earLUpper = getOrLogException(jointMap, "Ear_L_Upper");

        this.eyeR = getOrLogException(jointMap, "Eye_R");
        this.eyeL = getOrLogException(jointMap, "Eye_L");

        // Front Right
        this.shoulderFR = getOrLogException(jointMap, "Shoulder_FR");
        this.thighFR = getOrLogException(jointMap, "Thigh_FR");
        this.legFR = getOrLogException(jointMap, "Leg_FR");
        this.pawFR = getOrLogException(jointMap, "Paw_FR");

        // Front Left
        this.shoulderFL = getOrLogException(jointMap, "Shoulder_FL");
        this.thighFL = getOrLogException(jointMap, "Thigh_FL");
        this.legFL = getOrLogException(jointMap, "Leg_FL");
        this.pawFL = getOrLogException(jointMap, "Paw_FL");

        // Rear Right
        this.shoulderRR = getOrLogException(jointMap, "Shoulder_RR");
        this.thighRR = getOrLogException(jointMap, "Thigh_RR");
        this.legRR = getOrLogException(jointMap, "Leg_RR");
        this.pawRR = getOrLogException(jointMap, "Paw_RR");

        // Rear Left
        this.shoulderRL = getOrLogException(jointMap, "Shoulder_RL");
        this.thighRL = getOrLogException(jointMap, "Thigh_RL");
        this.legRL = getOrLogException(jointMap, "Leg_RL");
        this.pawRL = getOrLogException(jointMap, "Paw_RL");

        // Tail
        this.tail_0 = getOrLogException(jointMap, "Tail_0");
        this.tail_1 = getOrLogException(jointMap, "Tail_1");
        this.tail_2 = getOrLogException(jointMap, "Tail_2");
        this.tail_3 = getOrLogException(jointMap, "Tail_3");
        this.tail_4 = getOrLogException(jointMap, "Tail_4");


    }
}
