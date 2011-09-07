import processing.core.PApplet;

public class Integrator {
    
    PApplet parent;

    final float DAMPING = 0.5f;
    final float ATTRACTION = 0.2f;

    float value;
    float vel;
    float accel;
    float force;
    float mass = 1;

    float damping = DAMPING;
    float attraction = ATTRACTION;
    boolean targeting;
    float target;

    Integrator(PApplet p) {
        parent = p;
    }

    Integrator(PApplet p, float value) {
        parent = p;
        this.value = value;
    }

    Integrator(PApplet p, float value, float damping, float attraction) {
        parent = p;
        this.value = value;
        this.damping = damping;
        this.attraction = attraction;
    }

    void set(float v) {
        value = v;
    }

    void update() {
        if (targeting) {
            force += attraction * (target - value);
        }

        accel = force / mass;
        vel = (vel + accel) * damping;
        value += vel;

        force = 0;
    }

    void target(float t) {
        targeting = true;
        target = t;
    }

    void noTarget() {
        targeting = false;
    }
}
