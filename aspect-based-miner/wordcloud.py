import plotly.io as pio

# Feature list generated for the aspects after running the Spark job
# The words in ALL CAPS are used in negative meaning context and the words in ALL SMALL CAPS are used in positive meaning context.
op = "SIMPLE, SIMPLE, SIMPLE, first, love, AWFUL, DUMB, functional, handy, handy, handy, WORTH, great, great, great, easy, very, EXCESSIVE, love, great, pretty, cool, SENSITIVE, very, SENSITIVE, very, SENSITIVE, very, neat, pretty, convenient, love, intuitive, flexible, love, love, new, cool, new, nice, easy, convenient, BAD, make, NOVELTY, NOVELTY, new, love, love, upgrade, cool, new, love, I, convenient, thought, used, phenomenal, new, love, love, love, nice, use, colorful, love, convenient, use, interactive, TROUBLESOME, FEEL, SILLY, SURE, ONE, QUIET, love, convenient, super, touch, life, nice, love, new, SORT, STILL, just, high, dynamic, different, sound, beginning, amazing, OTHERWISE, love, fine, fine, fine, new, new, excellent, well, love, fast, fast, fast, love, handy, very, pleased, I, love, love, SATISFIED, EARLY, STILL, I, EARLY, STILL, I, EARLY, STILL, I, taking, true, true, true, HATE, easy, fine, fine, fine, USE, love, awesome, touch, great, possible, possible, possible, LIGHT, handy, pretty, new, love, amazing, love, advanced, MAGIC, amazing"
features = op.split(',')
# Count the number of positive and negative features in the list
positive = negative = 0
for feature in features:
    if (ord(feature.strip()[0]) >= 65 and ord(feature.strip()[0]) <= 90) :
        negative += 1
    elif (ord(feature.strip()[0]) >= 97 and ord(feature.strip()[0]) <= 122) :
        positive += 1

# Plotting the donut chart
labels = ['Positive','Negative']
values = [positive,negative]
# Setting the colours
color = ['rgb(69,250,241)', 'rgb(250,111,69)']
fig = {
  "data": [
    {
      "values": values,
      "labels": labels,
      "domain": {"x": [0.5, 0.5]},
      "hole": .6,
      "type": "pie",
      "marker": {"colors" : color},
    }],
  "layout": {
        "title":"",
        "annotations": [
            { "font": { "size": 20},
              "showarrow": False,
              "x": 0.5,
              "y": 0.5,
              'text':"Touchbar"
            },
        ]
    }
}
pio.show(fig)



