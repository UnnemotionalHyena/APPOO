require './rectangle'

class Parallelogram < Rectangle
  attr_accessor :h

  def initialize(a = Random.rand(1..20), b = Random.rand(1..20), height = Random.rand(1..20))
    if b < height
      height = Random.rand(1..b)
    end

    @x = a
    @y = b
    @h = height
  end

  def get_area
    @x * @h
  end

  def uniform_enlargement(percent)
    @x += @x * percent / 100.0
    @y += @y * percent / 100.0
    @h += @h * percent / 100.0
  end

  def output_edges
    puts "a = #{@x}", "b = #{@y}", "c = #{@x}", "z = #{@y}", "h = #{@h}"
  end
end

