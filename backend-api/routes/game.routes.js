module.exports = (app) => {
  const items = require("../controller/record.controller.js");
  var router = require("express").Router();

  router.get("/", items.findAll);
  router.put("/", items.updateRecords);
  app.use("/api/records", router);
};
