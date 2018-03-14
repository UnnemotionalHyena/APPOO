#ifndef HEADER_H
#define HEADER_H

#include <iostream>
using namespace std;

class Lichid
{
protected:
    char *name;
    float density;
public:
    Lichid ();
    Lichid (char *name, float density);
    void changeDensity (float new_value);
    char *get_name();
    float get_density();
};

class Alcohol: public Lichid
{
protected:
    int content;
public:
    Alcohol(char *name, float density, int content) : Lichid(name, density)
    {
        this->content = content;
    }
    void change_content(int new_value);
    int get_content();
};

class Wine: public Alcohol
{
    int sweetness;
public:
    Wine(char *name, float density, int content, int sweetness) : Alcohol(name, density, content)
    {
        this->sweetness = sweetness;
    }
    void change_sweetness(int new_value);
    int get_sweetness();
};

class OutputData
{
public:
    void output(Wine data);
    void output(Alcohol data);
    void output(Lichid data);
};

#endif
