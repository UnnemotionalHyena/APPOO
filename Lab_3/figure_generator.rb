require './square.rb'
require './rectangle.rb'
require './triangle.rb'

class FigureGenerator

def generate(fig = Square)
  @figure = fig.new
end

def show_perimeter
  puts "Perimeter: #{@figure.get_perimeter}"
end

def show_area
  puts "Area: #{@figure.get_area}"
end

def output_figure
  @figure.output_edges
end

end

