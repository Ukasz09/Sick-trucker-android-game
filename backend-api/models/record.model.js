module.exports = (mongoose) => {
  var schema = mongoose.Schema(
    {
      logoUrl: String,
      nick: String,
      travelledMeters: Number,
    },
    { timestamps: true }
  );

  schema.method("toJSON", function () {
    const { __v, _id, ...object } = this.toObject();
    object.id = _id;
    return object;
  });

  const Ranking = mongoose.model("ranking", schema);
  return Ranking;
};
