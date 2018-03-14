#include "header.hpp"

Lichid :: Lichid ()
{
    name = NULL;
    density = 0;
}

void OutputData::output(Lichid data)
{
    cout << "\nName: " << data.get_name() << endl;
    cout << "Density: " << data.get_density() << endl;
}

void OutputData::output(Alcohol data)
{
    cout << "\nName: " << data.get_name() << endl;
    cout << "Density: " << data.get_density() << endl;
    cout << "Content: " << data.get_content() << "%" << endl;
}

void OutputData::output(Wine data)
{
    cout << "\nName: " << data.get_name() << endl;
    cout << "Density: " << data.get_density() << endl;
    cout << "Content: " << data.get_content() << "%" << endl;
    cout << "Sweetness: " << data.get_sweetness() << "%" << endl;
}


void Lichid :: changeDensity (float new_value)
{
    density = new_value;
}

Lichid::Lichid(char *name, float density)
{
    this->name = name;
    this->density = density;
}

void Alcohol :: change_content(int new_value)
{
    content = new_value;
}

void Wine::change_sweetness(int new_value)
{
    sweetness = new_value;
}

char* Lichid::get_name()
{
    return name;
}

float Lichid::get_density()
{
    return density;
}

int Alcohol::get_content()
{
    return content;
}

int Wine::get_sweetness()
{
    return sweetness;
}