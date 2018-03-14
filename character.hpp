#ifndef _Character_H
#define _Character_H

#include <SDL2/SDL.h>
#include <SDL2/SDL_image.h>
#include <SDL2/SDL_ttf.h>
#include <iostream>
#include <string>

class character
{
public:
    bool stop_left = false;
    bool stop_right = false;
    bool stop = false;

    bool hit = false;

    character();
    ~character();
    bool loadFromFile(std::string path);

    void free();

    void render(int x, int y, SDL_Rect* clip, SDL_Point* center, SDL_RendererFlip flip);
    int getWidth();
    int getHeight();
    int get_position();
    void set_position(int x);
    int change_position(int x);
    void set_initial_position();

private:
    SDL_Texture* mTexture;

    int mWidth;
    int mHeight;
protected:
    int pPosition;
};

#endif