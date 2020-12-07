const db = require("../models");
const Ranking = db.ranking;

//Find all items with filters
exports.findAll = (req, res) => {
  const { sort } = req.query;
  let query = {};
  let sort_param = {};
  if (sort) {
    sort_param = { travelledMeters: sort };
  } else {
    sort_param = { createdAt: "desc" };
  }
  console.log(Ranking);
  //TODO: change to async
  //   Ranking.find(query)
  Ranking.find({})
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
