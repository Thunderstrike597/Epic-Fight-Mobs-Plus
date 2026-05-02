package net.kenji.epic_fight_mobs_plus.gameasset.armatures;

import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.model.Armature;

import java.util.Map;

public class WolfArmature extends Armature {
    public final Joint torso;
    public final Joint chest;

    public final Joint head;
    public final Joint snout;
    public final Joint jaw;
    public final Joint earR;
    public final Joint earL;
    public final Joint eyeR;
    public final Joint eyeL;
    public final Joint eyelidR;
    public final Joint eyelidL;
    // Front legs
    public final Joint shoulderFR;
    public final Joint thighFR;
    public final Joint legFR;

    public final Joint shoulderFL;
    public final Joint thighFL;
    public final Joint legFL;

    // Rear legs
    public final Joint shoulderRR;
    public final Joint thighRR;
    public final Joint legRR;

    public final Joint shoulderRL;
    public final Joint thighRL;
    public final Joint legRL;

    // Tail
    public final Joint tail_0;
    public final Joint tail_1;
    public final Joint tail_2;

    public WolfArmature(String name, int jointNumber, Joint rootJoint, Map<String, Joint> jointMap) {
        super(name, jointNumber, rootJoint, jointMap);

        // Core body
        this.torso = getOrLogException(jointMap, "Torso");
        this.chest = getOrLogException(jointMap, "Chest");

        // Head
        this.head = getOrLogException(jointMap, "Head");
        this.snout = getOrLogException(jointMap, "Snout");
        this.jaw = getOrLogException(jointMap, "Jaw");
        this.earR = getOrLogException(jointMap, "Ear_R");
        this.earL = getOrLogException(jointMap, "Ear_L");
        this.eyeR = getOrLogException(jointMap, "Eye_R");
        this.eyeL = getOrLogException(jointMap, "Eye_L");
        this.eyelidR = getOrLogException(jointMap, "EyeLid_R");
        this.eyelidL = getOrLogException(jointMap, "EyeLid_L");
        // Front Right
        this.shoulderFR = getOrLogException(jointMap, "Shoulder_FR");
        this.thighFR = getOrLogException(jointMap, "Thigh_FR");
        this.legFR = getOrLogException(jointMap, "Leg_FR");

        // Front Left
        this.shoulderFL = getOrLogException(jointMap, "Shoulder_FL");
        this.thighFL = getOrLogException(jointMap, "Thigh_FL");
        this.legFL = getOrLogException(jointMap, "Leg_FL");

        // Rear Right
        this.shoulderRR = getOrLogException(jointMap, "Shoulder_RR");
        this.thighRR = getOrLogException(jointMap, "Thigh_RR");
        this.legRR = getOrLogException(jointMap, "Leg_RR");

        // Rear Left
        this.shoulderRL = getOrLogException(jointMap, "Shoulder_RL");
        this.thighRL = getOrLogException(jointMap, "Thigh_RL");
        this.legRL = getOrLogException(jointMap, "Leg_RL");

        // Tail
        this.tail_0 = getOrLogException(jointMap, "Tail_0");
        this.tail_1 = getOrLogException(jointMap, "Tail_1");
        this.tail_2 = getOrLogException(jointMap, "Tail_2");
    }
}
