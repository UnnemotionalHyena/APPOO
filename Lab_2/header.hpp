#ifndef HEADER_H
#define HEADER_H

#include <iostream>
using namespace std;

class lichid
{
protected:
    char *name;
    float density;
public:
    lichid ();
    lichid (char *name, float density);
    void changeDensity (float new_value);
    void output_values();
};

class alcohol: public lichid
{
    int content;
    int sweetness;
public:
    alcohol(char *name, float density, int content, int sweetness) : lichid(name, density)
    {
        this->content = content;
        this->sweetness = sweetness;
    }
    void change_sweetness(int new_value);
    void change_content(int new_value);
    void output_values();
};

#endif
