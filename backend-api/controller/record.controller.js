const db = require("../models");
const Records = db.records;

exports.findAll = (req, res) => {
  const { sort } = req.query;
  let sort_param = {};
  if (sort) sort_param = { time: sort };
  else sort_param = { createdAt: "desc" };

  Records.find({})
    .sort(sort_param)
    .then((data) => {
      res.send(data);
    })
    .catch((err) => {
      res.status(500).send({
        message: err.message || "Some error occurred while retrieving items. ",
      });
    });
};
