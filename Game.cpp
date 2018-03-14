#include "Game.hpp"

extern const int SCREEN_WIDTH = 730;
extern const int SCREEN_HEIGHT = 580;

extern SDL_Window* gWindow;
extern SDL_Renderer* gRenderer;
extern const int WALKING_ANIMATION_FRAMES = 10;
extern SDL_Rect gSpriteClips[ WALKING_ANIMATION_FRAMES ];
extern SDL_Rect enemy_clips [ WALKING_ANIMATION_FRAMES ];
extern character batman;
extern character background;
extern enemy terror[2];

bool init()
{
    bool success = true;

    if( SDL_Init( SDL_INIT_VIDEO ) < 0 )
    {
        printf( "SDL could not initialize! SDL Error: %s\n", SDL_GetError() );
        success = false;
    }
    else
    {
        if( !SDL_SetHint( SDL_HINT_RENDER_SCALE_QUALITY, "1" ) )
        {
            printf( "Warning: Linear texture filtering not enabled!" );
        }

        gWindow = SDL_CreateWindow( "SDL Tutorial", SDL_WINDOWPOS_UNDEFINED, SDL_WINDOWPOS_UNDEFINED, SCREEN_WIDTH, SCREEN_HEIGHT, SDL_WINDOW_SHOWN );
        if( gWindow == NULL )
        {
            printf( "Window could not be created! SDL Error: %s\n", SDL_GetError() );
            success = false;
        }
        else
        {
            gRenderer = SDL_CreateRenderer( gWindow, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC );
            if( gRenderer == NULL )
            {
                printf( "Renderer could not be created! SDL Error: %s\n", SDL_GetError() );
                success = false;
            }
            else
            {
                SDL_SetRenderDrawColor( gRenderer, 0xFF, 0xFF, 0xFF, 0xFF );

                int imgFlags = IMG_INIT_PNG;
                if( !( IMG_Init( imgFlags ) & imgFlags ) )
                {
                    printf( "SDL_image could not initialize! SDL_image Error: %s\n", IMG_GetError() );
                    success = false;
                }
            }
        }
    }

    return success;
}

bool loadMedia()
{
    //Loading success flag
    bool success = true;

    //Load sprite sheet texture
    if( !batman.loadFromFile( "sprites/batman2.png" ) )
    {
        printf( "Failed to load batman sheet texture!\n" );
        success = false;
    }
    else
    {
        for (int i = 0; i < WALKING_ANIMATION_FRAMES; i++)
        {
            gSpriteClips[ i ].x = 77 * i;
            gSpriteClips[ i ].y =      0;
            gSpriteClips[ i ].w =     77;
            gSpriteClips[ i ].h =     63;
        }
    }

    int id = 0;
    for (auto &terrors : terror)
    {
        if (!terrors.loadFromFile( "sprites/batman_enemy.png" ))
        {
            printf( "Failed to load terror sheet texture!\n" );
            success = false;
        }
        else
        {
            terrors.id = id;
            ++id;
        }
    }
    for (int i = 0; i < WALKING_ANIMATION_FRAMES; i++)
    {
        {
            enemy_clips[ i ].x = 77 * i;
            enemy_clips[ i ].y =      0;
            enemy_clips[ i ].w =     77;
            enemy_clips[ i ].h =     63;
        }
    }

    if( !background.loadFromFile( "sprites/background.jpg" ) )
    {
        printf( "Failed to load background sheet texture!\n" );
        success = false;
    }

    return success;
}

void close()
{
    //Free loaded images
    batman.free();
    background.free();

    for (auto terrors : terror)
    {
        terrors.free();
    }

    //Destroy window    
    SDL_DestroyRenderer( gRenderer );
    SDL_DestroyWindow( gWindow );
    gWindow = NULL;
    gRenderer = NULL;

    //Quit SDL subsystems
    IMG_Quit();
    SDL_Quit();
}