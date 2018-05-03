require './square.rb'
require './rectangle.rb'
require './triangle.rb'
require './figure_generator'

generated_figure = FigureGenerator.new

generated_figure.generate(Triangle)

generated_figure.show_perimeter
generated_figure.show_area
generated_figure.output_figure