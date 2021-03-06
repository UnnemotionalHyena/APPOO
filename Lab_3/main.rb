require './square.rb'
require './rectangle.rb'
require './triangle.rb'
require './figure_generator'

generated_figure = FigureGenerator.new

[Triangle, Rectangle, Square].each do |figure|

  puts figure

  generated_figure.generate(figure)

  generated_figure.show_perimeter
  generated_figure.show_area
  generated_figure.output_figure
end