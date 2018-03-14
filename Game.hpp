#ifndef _Game_H
#define _Game_H

#include <SDL2/SDL.h>
#include <SDL2/SDL_image.h>
#include <SDL2/SDL_ttf.h>
#include <stdio.h>
#include <string>
#include "character.hpp"
#include "enemy.hpp"

bool init();
bool loadMedia();
void close();

#endif