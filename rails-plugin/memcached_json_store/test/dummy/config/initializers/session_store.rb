# Be sure to restart your server when you modify this file.

require 'action_dispatch/middleware/session/memcached_store'
Rails.application.config.session_store :memcached_store, :memcache_server => ['localhost'],
                                       :namespace => 'sessions', :key => '_foundation_session', :expire_after => 7.days, :domain => :all
