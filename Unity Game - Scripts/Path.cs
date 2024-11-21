// Include packages
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[RequireComponent(typeof(LineRenderer))]
public class Path : MonoBehaviour
{
    // Define variables
    private LineRenderer lineRenderer;
    private Vector3 last;
    private Vector3 next;
    private Vector3[] waypoints;

    private void OnDrawGizmos()
    {
        Gizmos.color = Color.yellow;

        last = transform.GetChild(0).position;
        for (int i = 1; i < transform.childCount; i++)
        {
            next = transform.GetChild(i).position;
            Gizmos.DrawLine(last, next);
            last = next;
        }
        
    }

    private void Start()
    {
        waypoints = new Vector3[transform.childCount];

        for (int i = 0; i < waypoints.Length; i++)
        {
            waypoints[i] = transform.GetChild(i).position;
        }

        lineRenderer = GetComponent<LineRenderer>();
        lineRenderer.positionCount = waypoints.Length;
        lineRenderer.SetPositions(waypoints);
    }

    public Vector3 Waypoint(int i)
    {
        return transform.GetChild(i).position;
    }

    // Getter for Length
    public int Length
    {
        get
        {
            return transform.childCount;
        }
    }
}
