$:.push File.expand_path("../lib", __FILE__)

# Maintain your gem's version:
require "memcached_json_store/version"

# Describe your gem and declare its dependencies:
Gem::Specification.new do |s|
  s.name        = "memcached_json_store"
  s.version     = MemcachedJsonStore::VERSION
  s.authors     = ["Amal Elshihaby"]
  s.email       = ["ashihaby@cloud9ers.com"]
  s.summary     = "A Bridge for sharing the session between play and rails by storing the session as json in the memcached"
  s.description = "Bridge for sharing the session between play and rails"

  s.files = Dir["{app,config,db,lib}/**/*", "MIT-LICENSE", "Rakefile", "README.rdoc"]
  s.test_files = Dir["test/**/*"]

  s.add_dependency "rails", "~> 4.0.0"
  s.add_dependency "dalli"

  s.add_development_dependency "sqlite3"
  s.add_development_dependency "devise"
end
