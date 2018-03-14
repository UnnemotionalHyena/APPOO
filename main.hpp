#ifndef MAIN_H
#define MAIN_H

const int SCREEN_WIDTH = 730;
const int SCREEN_HEIGHT = 580;

SDL_Window* gWindow = NULL;
SDL_Renderer* gRenderer = NULL;
const int WALKING_ANIMATION_FRAMES = 9;
SDL_Rect gSpriteClips[ WALKING_ANIMATION_FRAMES ];
SDL_Rect enemy_clips [ WALKING_ANIMATION_FRAMES ];
character batman;
character background;
enemy terror[2];

#endif