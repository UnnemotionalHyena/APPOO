#include "enemy.hpp"

int enemy::change_position(int x)
{
    if (initial_position == 0 && !stop)
    {
        if (pPosition < -80)
        {
            return pPosition = 810;
        }
        else
        {
            return pPosition -= x;
        }
    }
    else if (!stop)
    {
        if (pPosition > 815)
        {
            return pPosition == -80;
        }
        else
        {
            return pPosition += x;
        }
    }
    return pPosition;
}

int enemy::get_initial_position()
{
    return initial_position;
}

void enemy::set_initial_position(int i)
{
    initial_position = i;
    if (initial_position == 0)
    {
        pPosition = 810;
    }
    else
    {
        flip = SDL_FLIP_NONE;
        pPosition = -80;
    }
}