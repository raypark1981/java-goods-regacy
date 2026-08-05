window.onload = function() {
  var webjarsPath = "/webjars/";
  var pathName = window.location.pathname;
  var contextPath = pathName.indexOf(webjarsPath) >= 0
    ? pathName.substring(0, pathName.indexOf(webjarsPath))
    : "";

  window.ui = SwaggerUIBundle({
    url: contextPath + "/v3/api-docs",
    dom_id: "#swagger-ui",
    deepLinking: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout"
  });
};
