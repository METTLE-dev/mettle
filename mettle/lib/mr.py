import numpy as np
import pandas as pd
from pandas import DataFrame
from sklearn.decomposition import PCA
from sklearn.preprocessing import StandardScaler
import math
import random
import sys


def MR1_1(df):
    """
    reorder instances
    """
    sampler = np.random.permutation(len(df))
    df = df.take(sampler)
    return df


def MR1_2(df, df_c):
    """
    reorder instances while fix initial point
    """
    df = df.round({'A': 6, 'B': 6})
    df_c = df_c.round({'A': 6, 'B': 6})
    df['index'] = range(len(df))
    inst0 = df[['index', 'A', 'B']][df.A == df_c.iloc[0].A]
    inst1 = df[['index', 'A', 'B']][df.A == df_c.iloc[1].A]
    inst2 = df[['index', 'A', 'B']][df.A == df_c.iloc[2].A]
    frames = [inst0, inst1, inst2]
    df_i = pd.concat(frames)
    df_i = df_i.sort_values(['index'], ascending=True)
    df.drop(inst0.index, axis=0, inplace=True)
    df.drop(inst1.index, axis=0, inplace=True)
    df.drop(inst2.index, axis=0, inplace=True)
    df['index'] = range(len(df))
    # permutation
    sampler = np.random.permutation(len(df))
    df = df.take(sampler)
    for i in range(len(df_i)):
        above = df.iloc[:int(df_i.iloc[i, 0])]
        below = df.iloc[int(df_i.iloc[i, 0]):]
        df = above.append(df_i.iloc[i], ignore_index=True).append(below, ignore_index=True)
    return df[['A', 'B']]


def MR3_2(df):
    """
    add objects near boundary
    """
    a = np.random.randint(0, 4)
    cluster = df[['A', 'B']][df.cluster == a]        # randomly choose a cluster
    while len(cluster) < 5:
        a = np.random.randint(0, 3)
        cluster = df[['A', 'B']][df.cluster == a]
    test_data = [tuple(x) for x in cluster.values]
    result = convex_hull(test_data)
    x, y = get_interpolation(result)
    df_follow = pd.concat([df[['A', 'B']], DataFrame({'A': x, 'B': y, })])
    df_so = pd.concat([df, DataFrame({'A': x, 'B': y, 'cluster': a, })])
    return df_follow, df_so


def MR4_2(df):
    """
    remove redundancy
    """
    cov = [[1.0, 0.9], [0.9, 1.0]]
    L = np.linalg.cholesky(cov)
    uncorrelated = df.A, df.B
    correlated = np.dot(L, uncorrelated)
    a, a1 = correlated
    df['A1'] = a1
    return df


def MR5_1(df):
    """
    rotation
    """
    def rotation(x, y, a):
        l = a*(math.pi/180)
        cos_a = math.cos(l)
        sin_a = math.sin(l)
        x1 = x*cos_a + y*sin_a
        y1 = y*cos_a - x*sin_a
        return x1, y1
    rotation_func = np.frompyfunc(rotation, 3, 2)
    a = np.random.randint(0, 90)
    x, y = rotation_func(df.A, df.B, a)
    x = x.astype(np.float)
    y = y.astype(np.float)
    df_new = DataFrame({'A': x, 'B': y, })
    return df_new


def MR5_2(df):
    """
    scale
    """
    def affine(x, a):
        return x*a
    resize_ufunc = np.frompyfunc(affine, 2, 1)
    a = np.random.uniform(0.2, 5)
    a = round(a, 1)
    x = resize_ufunc(df.A, a)
    y = resize_ufunc(df.B, a)
    x = x.astype(np.float)
    y = y.astype(np.float)
    df_new = DataFrame({'A': x, 'B': y, })
    return df_new


def MR_6(df):
    """
    add noise
    """
    x = np.random.randint(15, 19)
    y = np.random.randint(2, 10)
    df = df.append({'A': x, 'B': y}, ignore_index=True)
    return df


def pca(df, n_components):
    x = df.values
    pca = PCA(n_components)
    x_new = pca.fit_transform(x)
    return pd.DataFrame(x_new)


def scale(df):
    x = df.values
    x_scale = StandardScaler().fit_transform(x)
    return pd.DataFrame(x_scale)


def get_leftbottompoint(p):
    k = 0
    for i in range(1, len(p)):
        if p[i][1] < p[k][1] or (p[i][1] == p[k][1] and p[i][0] < p[k][0]):
            k = i
    return k


def multiply(p1, p2, p0):
    return (p1[0] - p0[0]) * (p2[1] - p0[1]) - (p2[0] - p0[0]) * (p1[1] - p0[1])


def get_arc(p1, p0):
    if (p1[0] - p0[0]) == 0:
        if (p1[1] - p0[1]) == 0:
            return -1
        else:
            return math.pi / 2
    tan = float((p1[1] - p0[1])) / float((p1[0] - p0[0]))
    arc = math.atan(tan)
    if arc >= 0:
        return arc
    else:
        return math.pi + arc
    return -1;


def sort_points_tan(p, k):
    p2 = []
    for i in range(0, len(p)):
        p2.append({"index": i, "arc": get_arc(p[i], p[k])})
    p2.sort(key=lambda k: (k.get('arc', 0)))
    p_out = []
    for i in range(0, len(p2)):
        p_out.append(p[p2[i]["index"]])
    return p_out


def convex_hull(p):
    p = list(set(p))
    k = get_leftbottompoint(p)
    p_sort = sort_points_tan(p, k)
    p_result = [None] * len(p_sort)
    p_result[0] = p_sort[0]
    p_result[1] = p_sort[1]
    p_result[2] = p_sort[2]
    top = 2
    for i in range(3, len(p_sort)):
        while top >= 1 and multiply(p_sort[i], p_result[top], p_result[top - 1]) > 0:
            top -= 1
        top += 1
        p_result[top] = p_sort[i]

    for i in range(len(p_result) - 1, -1, -1):
        if p_result[i] is None:
            p_result.pop()

    return p_result


def cal_interpolation(x0, y0, x1, y1):
    x = random.uniform(x0, x1)
    y = y0 + (y1 - y0) * (x - x0) / (x1 - x0)
    return x, y


def get_interpolation(result):
    result_n = random.sample(result, 3)
    x = [[0] for i in range(3)]
    y = [[0] for i in range(3)]
    m = [[0] for i in range(12)]
    n = [[0] for i in range(12)]
    for i in range(3):
        tup = result_n[i]
        x[i], y[i] = tup
    for i in range(4):
        m[i], n[i] = cal_interpolation(x[0], y[0], x[1], y[1])
    for i in range(4, 8):
        m[i], n[i] = cal_interpolation(x[1], y[1], x[2], y[2])
    for i in range(8, 12):
        m[i], n[i] = cal_interpolation(x[0], y[0], x[2], y[2])
    return m, n


if __name__ == '__main__':
    sourceFile = sys.argv[1]
    followFile = sys.argv[2]
    methodName = sys.argv[-1]
    df_f = eval(methodName)(pd.read_csv(sourceFile))
    df_f.to_csv(followFile, index=False)
