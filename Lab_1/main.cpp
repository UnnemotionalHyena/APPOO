#include <SDL2/SDL.h>
#include <SDL2/SDL_image.h>
#include <SDL2/SDL_ttf.h>
#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <time.h>

#include "character.hpp"
#include "Game.hpp"
#include "main.hpp"
#include "enemy.hpp"
const int SPEED = 5;

#define enemy_low 10
#define enemy_medium 5
#define enemy_hard 1


const int enemy_lvl = enemy_hard;

int main( int argc, char* args[] )
{
    bool right_flag = true;
    bool STOP_FLAG = true;

    int hit_count = 0;
    int hit_count_animation = 0;
    int nr_enemys = 0;
    int enemy_waiting_hit = 0;
    int score_count = 0;
    batman.set_initial_position();
    SDL_RendererFlip flipType = SDL_FLIP_NONE;


    if( !init() )
    {
        printf( "Failed to initialize!\n" );
    }
    else
    {
        if( !loadMedia() )
        {
            printf( "Failed to load media!\n" );
        }
        else
        {   
            bool quit = false;

            SDL_Event e;

            int frame = 0;
            long long int global_frames = 0;
            terror[0].set_initial_position(0);
            terror[1].set_initial_position(1);

            while( !quit )
            {
                while( SDL_PollEvent( &e ) != 0 )
                {
                    if( e.type == SDL_QUIT )
                    {
                        quit = true;
                    }
                    else if ( e.type == SDL_KEYDOWN)
                    {
                        switch( e.key.keysym.sym )
                        {
                            case SDLK_a: case SDLK_LEFT:
                            hit_count = 0;
                            if (right_flag == true)
                            {
                                flipType = SDL_FLIP_HORIZONTAL;
                                right_flag = false;
                            }

                            if (!batman.stop_left)
                            {
                                STOP_FLAG = false;

                                batman.change_position(-10);
                                if (batman.get_position() < -10)
                                {
                                    batman.set_position(-10);
                                }
                            }
                            break;

                            case SDLK_d: case SDLK_RIGHT:
                            hit_count = 0;
                            if (right_flag == false)
                            {
                                flipType = SDL_FLIP_NONE;
                                right_flag = true;
                            }

                            if (!batman.stop_right)
                            {
                                STOP_FLAG = false;

                                batman.change_position(10);
                                if (batman.get_position() > 660)
                                {
                                    batman.set_position(660) ;
                                }
                            }

                            break;

                            case SDLK_SPACE:
                            batman.hit = true;
                            hit_count_animation = global_frames;
                            hit_count += 1;

                            default:
                            STOP_FLAG = true;
                            break;
                        }
                    }
                    else if ( e.type == SDL_KEYUP)
                    {
                        switch( e.key.keysym.sym )
                        {
                            case SDLK_a: case SDLK_LEFT:
                            STOP_FLAG = true;
                            break;

                            case SDLK_d: case SDLK_RIGHT:
                            STOP_FLAG = true;
                            break;

                            case SDLK_SPACE:
                            batman.hit = false;
                            hit_count = 0;

                            default:
                            STOP_FLAG = true;
                            break;
                        }
                    }
                }

                SDL_SetRenderDrawColor( gRenderer, 0xFF, 0xFF, 0xFF, 0xFF );
                SDL_RenderClear( gRenderer );
                SDL_Rect* currentClip, *currentClip2;
                if (STOP_FLAG == true)
                {
                    currentClip = &gSpriteClips[0];
                }
                else
                {
                    currentClip = &gSpriteClips[ frame / SPEED ];
                }

                currentClip2 = &enemy_clips[ frame / SPEED ];

                background.render(0,0, NULL, NULL, SDL_FLIP_NONE);

                if (batman.get_position() > terror[0].get_position() - 50)
                {

                    terror[0].stop = true;
                    currentClip2 = &enemy_clips[0];
                    batman.stop_right = true;

                    if (batman.hit && hit_count == 1 && flipType == SDL_FLIP_NONE)
                    {   
                        terror[0].set_position(810);
                        score_count += 1;
                    }
                }
                
                if (batman.get_position() < terror[1].get_position() + 50)
                {
                    terror[1].stop = true;
                    currentClip2 = &enemy_clips[0];
                    batman.stop_left = true;

                    if (batman.hit && hit_count == 1 && flipType == SDL_FLIP_HORIZONTAL)
                    {
                        terror[1].set_position(-80);
                        score_count += 1;
                    }
                }
                if (!(batman.get_position() > terror[0].get_position() - 40))
                {
                    terror[0].stop = false;
                    batman.stop_right = false;
                }
                if (!(batman.get_position() < terror[1].get_position() + 40))
                {
                    terror[1].stop = false;
                    batman.stop_left = false;
                }

                if ((global_frames - hit_count_animation) < 6)
                {
                    batman.stop_left = true;
                    batman.stop_right = true;
                    currentClip = &gSpriteClips[9];
                }
                for (int i = 0; i < 2; i++)
                {
                    if (terror[i].stop && !terror[i].wait)
                    {
                        terror[i].enemy_waiting_hit = global_frames;
                        terror[i].wait = true;
                    }
                    if (terror[i].wait)
                    {
                        terror[i].stop = true;
                    }
                    if ((global_frames - terror[i].enemy_waiting_hit) > enemy_hard && terror[i].wait)
                    {
                        currentClip2 = &enemy_clips[9];
                        if ((batman.get_position() > terror[0].get_position() - 50) ||
                            (batman.get_position() < terror[1].get_position() + 50))
                        {
                            printf("\n\n\tYour score is : %d\n", score_count);
                            printf("\n\n\t   GAME OVER\n");
                            close();
                            return 0;
                        }
                        terror[i].stop = false;
                        terror[i].wait = false;
                    }

                }

                terror[0].render( terror[0].change_position(2), ( SCREEN_HEIGHT - currentClip2->h ) / 2 + 220, currentClip2, 0, terror[0].flip);
                terror[1].render( terror[1].change_position(2), ( SCREEN_HEIGHT - currentClip2->h ) / 2 + 220, currentClip2, 0, terror[1].flip);
                batman.render( batman.get_position(), ( SCREEN_HEIGHT - currentClip->h ) / 2 + 220, currentClip, 0, flipType);
                SDL_RenderPresent( gRenderer );

                ++frame;

                if (global_frames < 540)
                {
                    nr_enemys = global_frames / 180;
                    ++global_frames;
                }
                else
                {
                    ++global_frames;
                }

                if( frame / SPEED >= WALKING_ANIMATION_FRAMES )
                {
                    frame = 0;
                }
            }
        }
    }

    close();

    return 0;
}