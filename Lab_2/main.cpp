#include "header.hpp"
int main()
{
    OutputData outputer;
    Alcohol alcohol("Vodka", 0.99392, 45);
    Wine cagor("Cagor", 0.98392, 12, 60);
    outputer.output(alcohol);
    outputer.output(cagor);
    cout << "\t\t\n\nChange content and density\n";
    cagor.change_content(10);
    cagor.changeDensity(0.96546);
    outputer.output(cagor);

    return 0;
}