tutSettings

watchSources <++= tutSourceDirectory map { path => (path ** "*.html").get }
