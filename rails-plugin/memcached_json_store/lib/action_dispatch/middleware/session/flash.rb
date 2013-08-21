module ActionDispatch
  class Flash
    class FlashHash

      attr_accessor :used, :closed, :flashes, :now

      def self.json_create(o)
        obj = new()
        obj.used = o['data']['used']
        obj.closed = o['data']['closed']
        obj.now = o['data']['now']
        obj.flashes = o['data']['flashes']
        obj
      end

      def to_json(*a)
        { 'json_class' => self.class.name, 'data' => {closed: @closed, flashes: @flashes, now: @now, used: @used}}.to_json(*a)
      end

    end
  end
end

class Set
  def as_json(options = { })
    {
        "json_class" => self.class.name,
        "data" => { "elements" => self.to_a }
    }
  end
  def to_json(*a)
    as_json.to_json(*a)
  end
  def self.json_create(o)
    new o["data"]["elements"]
  end
end