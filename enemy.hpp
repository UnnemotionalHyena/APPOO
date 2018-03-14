#ifndef Enemy_H
#define Enemy_H

#include <iostream>
#include <random>
#include <time.h>
#include "character.hpp"

class enemy : public character
{
public:
    SDL_RendererFlip flip;
    int id;
    int enemy_waiting_hit;
    bool wait;

    enemy() 
    {
        wait = false;
        int enemy_waiting_hit = 0;
        flip = SDL_FLIP_HORIZONTAL;
        pPosition = 810;
    };

    int change_position();
    int get_initial_position();
    void set_initial_position(int i);
private:
    int initial_position;
};

#endif