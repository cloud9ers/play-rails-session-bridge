require 'action_dispatch/middleware/session/dalli_store'
require 'action_dispatch/middleware/session/flash'
require 'json'

class MemcachedStore  < ActionDispatch::Session::DalliStore
  def set_session(env, sid, session_data, options = nil)
    options ||= env[ENV_SESSION_OPTIONS_KEY]
    expiry  = options[:expire_after]
    @pool.set(sid, JSON.dump(session_data), expiry, :raw => true)
    sid
  rescue Dalli::DalliError
    Rails.logger.warn("Session::DalliStore#set: #{$!.message}")
    raise if @raise_errors
    false
  end

  def get_session(env, sid)
    sid ||= generate_sid
    begin
      session = JSON.load(@pool.get(sid) || '{}')  rescue {}
    rescue Dalli::DalliError => ex
      # re-raise ArgumentError so Rails' session abstract_store.rb can autoload any missing models
      raise ArgumentError, ex.message if ex.message =~ /unmarshal/
      Rails.logger.warn("Session::DalliStore#get: #{ex.message}")
      session = {}
    end
    [sid, session]
  end

end