require './rectangle'

class Square < Rectangle

  def initialize(size = Random.rand(1..20))
    @x = size
    @y = @x
  end
end