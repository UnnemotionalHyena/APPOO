require './square.rb'
require './rectangle.rb'
require './triangle.rb'
require './parallelogram'
require './figure_generator'

generated_figure = FigureGenerator.new

[Triangle, Rectangle, Square, Parallelogram].each do |figure|

  puts "\n#{figure}"

  generated_figure.generate(figure)

  generated_figure.show_perimeter
  generated_figure.show_area
  generated_figure.output_figure
end