#include "Game.hpp"
#include "character.hpp"

extern SDL_Renderer* gRenderer;

character::character()
{
    mTexture = NULL;
    mWidth = 0;
    mHeight = 0;
    pPosition = 0;
}

character::~character()
{
    free();
}

bool character::loadFromFile(std::string path)
{
    free();

    SDL_Texture* newTexture = NULL;

    SDL_Surface* loadedSurface = IMG_Load( path.c_str() );
    if( loadedSurface == NULL )
    {
        printf( "Unable to load image %s! SDL_image Error: %s\n", path.c_str(), IMG_GetError() );
    }
    else
    {
        SDL_SetColorKey( loadedSurface, SDL_TRUE, SDL_MapRGB( loadedSurface->format, 0, 0xFF, 0xFF ) );
        newTexture = SDL_CreateTextureFromSurface( gRenderer, loadedSurface );
        if( newTexture == NULL )
        {
            printf( "Unable to create texture from %s! SDL Error: %s\n", path.c_str(), SDL_GetError() );
        }
        else
        {
            mWidth = loadedSurface->w;
            mHeight = loadedSurface->h;
        }

        SDL_FreeSurface( loadedSurface );
    }

    mTexture = newTexture;
    return mTexture != NULL;
}

void character::render( int x, int y, SDL_Rect* clip, SDL_Point* center, SDL_RendererFlip flip)
{
    SDL_Rect renderQuad = { x, y, mWidth, mHeight };

    if( clip != NULL )
    {
        renderQuad.w = clip->w;
        renderQuad.h = clip->h;
    }

    SDL_RenderCopyEx( gRenderer, mTexture, clip, &renderQuad, 0, NULL, flip );
}

void character::free()
{
    if( mTexture != NULL )
    {
        SDL_DestroyTexture( mTexture );
        mTexture = NULL;
        mWidth = 0;
        mHeight = 0;
    }
}

int character::getWidth()
{
    return mWidth;
}

int character::getHeight()
{
    return mHeight;
}

int character::change_position(int x)
{
    return pPosition += x;
}

int character::get_position()
{
    return pPosition;
}

void character::set_position(int x)
{
    pPosition = x;
}

void character::set_initial_position()
{
    pPosition = 730 / 2 - 77;
}