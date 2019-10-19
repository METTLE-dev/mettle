import pandas as pd
import sys


def reindex(target_file, num):
    names = locals()
    df = pd.read_csv(target_file)
    df = df.sort_values(['A'], ascending=True)
    df['index'] = range(len(df))

    dict = {}
    for i in range(num):
        names['sub%s' % str(i)] = df[['index', 'A']][df.cluster == i]
        try:
            dict['%s' % str(i)] = names['sub%s' % str(i)].iloc[0, 0]  # value of the first instance
        except IndexError:
            dict['%s' % str(i)] = 99
    dict_sorted = sorted(dict.items(), key=lambda x: x[1])    # sort by value
    for index, item in enumerate(dict_sorted):
        names['sub%s' % item[0]]['cluster'] = index

    df_new = pd.concat([names['sub0'], names['sub1'], names['sub2']])
    df_new = df_new.sort_values(['index'], ascending=True)

    return df_new


def compare(source, follow):
    re = 0
    count = len(source)
    follow = follow[:count]
    for i in range(count):
        try:
            if source.iloc[i].cluster != follow.iloc[i].cluster:
                re += 1
        except Exception:
            pass
    return float(re*100) / count


if __name__ == '__main__':
    sourceFile = sys.argv[1]
    followFile = sys.argv[2]
    num = int(sys.argv[-1])
    re = compare(reindex(sourceFile, num), reindex(followFile, num))
    print(re)
