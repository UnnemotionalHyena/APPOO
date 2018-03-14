#include "header.hpp"

lichid :: lichid ()
{
    name = NULL;
    density = 0;
}

void lichid::output_values()
{
    cout << "\nName: " << name << "\n";
    cout << "Density: " << density;
}

void alcohol::output_values()
{
    cout << "\nName: " << name << endl;
    cout << "Density: " << density << endl;
    cout << "Content: " << content << "%" << endl;
    cout << "Sweetness: " << sweetness << "%" << endl;
}

void lichid :: changeDensity (float new_value)
{
    density = new_value;
}

lichid::lichid(char *name, float density)
{
    this->name = name;
    this->density = density;
}

void alcohol :: change_content(int new_value)
{
    content = new_value;
}

void alcohol::change_sweetness(int new_value)
{
    sweetness = new_value;
}