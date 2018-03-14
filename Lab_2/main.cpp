#include "header.hpp"
int main()
{
    alcohol a("Cagor", 0.98392, 12, 35);

    a.output_values();
    cout << "\t\t\n\nChange content and density\n";
    a.change_content(10);
    a.changeDensity(0.96546);
    a.output_values();
    
    return 0;
}